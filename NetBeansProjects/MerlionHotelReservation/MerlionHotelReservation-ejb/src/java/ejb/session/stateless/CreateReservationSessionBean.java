/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerReservation;
import entity.Employee;
import entity.ReservationDetails; // Ensure the correct entity name is used here
import entity.RoomRate;
import entity.RoomRate.RateType;
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

@Stateless
public class CreateReservationSessionBean implements CreateReservationSessionBeanRemote, CreateReservationSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @Override
    public List<RoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomType> availableRoomTypes = new ArrayList<>();
        
        // Retrieve all room types
        List<RoomType> allRoomTypes = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();

        for (RoomType roomType : allRoomTypes) {
            if (isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
                availableRoomTypes.add(roomType);
            }
        }
        
        return availableRoomTypes;
    }

    private boolean isRoomTypeAvailable(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        Query query = em.createQuery("SELECT COUNT(rd) FROM ReservationDetail rd WHERE rd.roomType = :roomType AND rd.reservationDate BETWEEN :checkInDate AND :checkOutDate");
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate.minusDays(1));

        Long reservedCount = (Long) query.getSingleResult();
        int availableRooms = roomType.getCapacity();

        return reservedCount < availableRooms;
    }
    
    @Override
    public CustomerReservation createReservation(Customer customer, LocalDate checkInDate, LocalDate checkOutDate, double totalCost) {
        CustomerReservation reservation = new CustomerReservation();
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setTotalCost(totalCost);
        reservation.setStatus(CustomerReservation.ReservationStatus.PENDING);
        reservation.setCustomer(customer);

        em.persist(reservation);
        em.flush();

        customer.getReservations().add(reservation);
        em.merge(customer);

        return reservation;
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
        List<RoomRate> rates = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomType = :roomType AND :date BETWEEN r.startDate AND r.endDate", RoomRate.class)
                                  .setParameter("roomType", roomType)
                                  .setParameter("date", date)
                                  .getResultList();

        BigDecimal applicableRate = null;

        for (RoomRate rate : rates) {
            if (rate.getRateType() == RateType.PROMOTION) {
                applicableRate = rate.getRatePerNight();
                break;
            } else if (rate.getRateType() == RateType.PEAK && (applicableRate == null || applicableRate.compareTo(rate.getRatePerNight()) < 0)) {
                applicableRate = rate.getRatePerNight();
            } else if (rate.getRateType() == RateType.NORMAL && (applicableRate == null || applicableRate.compareTo(rate.getRatePerNight()) < 0)) {
                applicableRate = rate.getRatePerNight();
            } else if (rate.getRateType() == RateType.PUBLISHED && applicableRate == null) {
                applicableRate = rate.getRatePerNight();
            }
        }

        return applicableRate != null ? applicableRate : BigDecimal.ZERO;
    }

    public ReservationDetails addReservationDetail(CustomerReservation reservation, LocalDate reservationDate, double priceForNight, RoomType roomType) { 
        ReservationDetails reservationDetail = new ReservationDetails();
        reservationDetail.setReservationDate(reservationDate);
        reservationDetail.setPriceForNight(priceForNight);
        reservationDetail.setReservation(reservation);
        reservationDetail.setRoomType(roomType);

        em.persist(reservationDetail);
        em.flush();

        reservation.getReservationDetails().add(reservationDetail);
        em.merge(reservation);

        return reservationDetail;
    }
        
    public void updateReservationStatus(Long reservationId, CustomerReservation.ReservationStatus newStatus) throws Exception {
        CustomerReservation reservation = em.find(CustomerReservation.class, reservationId);

        if (reservation == null) {
            throw new Exception("Reservation not found.");
        }

        CustomerReservation.ReservationStatus currentStatus = reservation.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new Exception("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        reservation.setStatus(newStatus);
        em.merge(reservation);
    }

    private boolean isValidStatusTransition(CustomerReservation.ReservationStatus currentStatus, CustomerReservation.ReservationStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == CustomerReservation.ReservationStatus.CONFIRMED || newStatus == CustomerReservation.ReservationStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == CustomerReservation.ReservationStatus.CHECKED_IN || newStatus == CustomerReservation.ReservationStatus.CANCELLED;
            case CHECKED_IN:
                return newStatus == CustomerReservation.ReservationStatus.CHECKED_OUT;
            case CANCELLED:
            case CHECKED_OUT:
                return false;
            default:
                return false;
        }
    }

    public CustomerReservation viewCustomerReservation(Long customerId, Long reservationId) {
        Customer customer = em.find(Customer.class, customerId);
        if (customer != null) {
            return customer.getReservations().stream()
                           .filter(reservation -> reservation.getReservationId().equals(reservationId))
                           .findFirst()
                           .orElse(null);
        }
        return null;
    }

    public List<CustomerReservation> viewAllReservations() {
        Query query = em.createQuery("Select r from CustomerReservation r");
        return query.getResultList();
    }

    public CustomerReservation reserveHotelRoom(Customer customer, List<Long> roomTypeIds, LocalDate checkInDate, LocalDate checkOutDate) throws Exception {
        if (customer == null) {
            throw new Exception("Customer not found.");
        }

        List<ReservationDetails> reservationDetails = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Long roomTypeId : roomTypeIds) {
            RoomType roomType = em.find(RoomType.class, roomTypeId);
            if (roomType == null) {
                throw new Exception("Room Type with ID " + roomTypeId + " not found.");
            }

            if (!isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
                throw new Exception("Room Type " + roomType.getName() + " is not available for the selected dates.");
            }

            BigDecimal roomTotalCost = calculateTotalCost(roomType, checkInDate, checkOutDate);
            totalCost = totalCost.add(roomTotalCost);

            LocalDate currentDate = checkInDate;
            while (!currentDate.isAfter(checkOutDate.minusDays(1))) {
                BigDecimal nightlyRate = getApplicableRate(roomType, currentDate);
                ReservationDetails detail = new ReservationDetails();
                detail.setReservationDate(currentDate);
                detail.setPriceForNight(nightlyRate.doubleValue());
                detail.setRoomType(roomType);
                detail.setReservation(null);

                reservationDetails.add(detail);
                currentDate = currentDate.plusDays(1);
            }
        }

        CustomerReservation reservation = createReservation(customer, checkInDate, checkOutDate, totalCost.doubleValue());
        for (ReservationDetails detail : reservationDetails) {
            detail.setReservation(reservation);
            em.persist(detail);
            reservation.getReservationDetails().add(detail);
        }
        
        em.merge(reservation);

        if (checkInDate.equals(LocalDate.now()) && LocalTime.now().isAfter(LocalTime.of(2, 0))) {
            allocateRoomsImmediately(reservation);
        }

        return reservation;
    }

    private void allocateRoomsImmediately(CustomerReservation reservation) {
        for (ReservationDetails detail : reservation.getReservationDetails()) {
            System.out.println("Allocating room for " + detail.getReservationDate() + " for reservation ID: " + reservation.getReservationId());
        }
        reservation.setStatus(CustomerReservation.ReservationStatus.CONFIRMED);
        em.merge(reservation);
    }
}
