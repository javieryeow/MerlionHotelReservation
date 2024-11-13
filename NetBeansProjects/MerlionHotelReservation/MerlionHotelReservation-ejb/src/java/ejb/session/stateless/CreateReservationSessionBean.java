/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidStatusTransitionException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;

@Stateless
public class CreateReservationSessionBean implements CreateReservationSessionBeanRemote, CreateReservationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @Override
    public List<RoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomType> availableRoomTypes = new ArrayList<>();

        List<RoomType> allRoomTypes = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();

        for (RoomType roomType : allRoomTypes) {
            if (isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
                availableRoomTypes.add(roomType);
            }
        }

        return availableRoomTypes;
    }

    private boolean isRoomTypeAvailable(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :roomType AND r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate");
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        Long reservedCount = (Long) query.getSingleResult();
        int availableRooms = roomType.getCapacity();

        return reservedCount < availableRooms;
    }

    @Override
    public BigDecimal calculateTotalCost(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        BigDecimal totalCost = BigDecimal.ZERO;
        LocalDate currentDate = checkInDate;

        while (!currentDate.isAfter(checkOutDate.minusDays(1))) {
            BigDecimal nightlyRate = getApplicableRate(roomType, currentDate);
            totalCost = totalCost.add(nightlyRate);
            currentDate = currentDate.plusDays(1);
        }

        return totalCost;
    }

    private BigDecimal getApplicableRate(RoomType roomType, LocalDate date) {
        List<RoomRate> rates = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomType = :roomType AND :date BETWEEN r.startDate AND r.endDate AND r.enabled = true", RoomRate.class)
                                  .setParameter("roomType", roomType)
                                  .setParameter("date", date)
                                  .getResultList();

        BigDecimal applicableRate = BigDecimal.ZERO;

        for (RoomRate rate : rates) {
            if (rate.getRateType() == RoomRate.RateType.PROMOTION) {
                return rate.getRatePerNight();
            } else if (rate.getRateType() == RoomRate.RateType.PEAK) {
                applicableRate = applicableRate.max(rate.getRatePerNight());
            } else if (rate.getRateType() == RoomRate.RateType.NORMAL && applicableRate.compareTo(rate.getRatePerNight()) < 0) {
                applicableRate = rate.getRatePerNight();
            } else if (rate.getRateType() == RoomRate.RateType.PUBLISHED && applicableRate.equals(BigDecimal.ZERO)) {
                applicableRate = rate.getRatePerNight();
            }
        }

        return applicableRate;
    }

   public void updateReservationStatus(Long reservationId, Reservation.ReservationStatus newStatus) 
        throws ReservationNotFoundException, InvalidStatusTransitionException {
    
    Reservation reservation = em.find(Reservation.class, reservationId);

    if (reservation == null) {
        throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.");
    }

    Reservation.ReservationStatus currentStatus = reservation.getStatus();

    if (!isValidStatusTransition(currentStatus, newStatus)) {
        throw new InvalidStatusTransitionException(
            "Invalid status transition from " + currentStatus + " to " + newStatus
        );
    }

    reservation.setStatus(newStatus);
    em.merge(reservation);
}

    private boolean isValidStatusTransition(Reservation.ReservationStatus currentStatus, Reservation.ReservationStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == Reservation.ReservationStatus.CONFIRMED || newStatus == Reservation.ReservationStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == Reservation.ReservationStatus.CHECKED_IN || newStatus == Reservation.ReservationStatus.CANCELLED;
            case CHECKED_IN:
                return newStatus == Reservation.ReservationStatus.CHECKED_OUT;
            case CANCELLED:
            case CHECKED_OUT:
                return false;
            default:
                return false;
        }
    }

    @Override
    public Reservation viewCustomerReservation(Long customerId, Long reservationId) {
        Customer customer = em.find(Customer.class, customerId);
        if (customer != null) {
            return customer.getReservations().stream()
                           .filter(reservation -> reservation.getReservationId().equals(reservationId))
                           .findFirst()
                           .orElse(null);
        }
        return null;
    }

    @Override
    public List<Reservation> viewAllReservations(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        return customer != null ? customer.getReservations() : new ArrayList<>();
    }

    @Override
  public Reservation reserveHotelRoom(Customer customer, List<Long> roomTypeIds, LocalDate checkInDate, LocalDate checkOutDate) 
        throws RoomTypeNotFoundException, RoomTypeUnavailableException {
    
    BigDecimal totalCost = BigDecimal.ZERO;
    List<RoomRate> applicableRates = new ArrayList<>();
    RoomType selectedRoomType = null;

    for (Long roomTypeId : roomTypeIds) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }

        if (!isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
            throw new RoomTypeUnavailableException("Room Type " + roomType.getName() + " is not available for the selected dates.");
        }

        totalCost = totalCost.add(calculateTotalCost(roomType, checkInDate, checkOutDate));
        selectedRoomType = roomType;
    }

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setTotalCost(totalCost);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        reservation.setCustomer(customer);
        reservation.setRoomType(selectedRoomType); // Set selected room type

        em.persist(reservation);
        em.flush();

        if (customer.getReservations() == null) {
            customer.setReservations(new ArrayList<>());
        }
        customer.getReservations().add(reservation);
        em.merge(customer);

        if (checkInDate.equals(LocalDate.now()) && LocalTime.now().isAfter(LocalTime.of(2, 0))) {
            allocateRoomsImmediately(reservation);
        }

        return reservation;
    }

    private void allocateRoomsImmediately(Reservation reservation) {
        System.out.println("Allocating rooms for reservation ID: " + reservation.getReservationId());
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        em.merge(reservation);
    }
}


