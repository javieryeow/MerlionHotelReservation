/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.RoomRate;
import entity.RoomRate.RateType;
import entity.RoomType;
import entity.WalkInGuest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
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
    public List<RoomType> searchAvailableRooms(Date checkInDate, Date checkOutDate) {
        List<RoomType> availableRoomTypes = new ArrayList<>();

        List<RoomType> allRoomTypes = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();

        for (RoomType roomType : allRoomTypes) {
            if (isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
                availableRoomTypes.add(roomType);
            }
        }

        return availableRoomTypes;
    }

    private boolean isRoomTypeAvailable(RoomType roomType, Date checkInDate, Date checkOutDate) {
        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :roomType AND r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate");
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        Long reservedCount = (Long) query.getSingleResult();
        int availableRooms = roomType.getCapacity();

        return reservedCount < availableRooms;
    }

    @Override
   public BigDecimal calculateTotalCost(RoomType roomType, Date checkInDate, Date checkOutDate) {
    BigDecimal totalCost = BigDecimal.ZERO;

    // Use Calendar for date manipulation
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(checkInDate);

    // Loop through each night from check-in to the day before check-out
    while (calendar.getTime().before(checkOutDate)) {
        Date currentDate = calendar.getTime();
        BigDecimal nightlyRate = getApplicableRate(roomType, currentDate);
        totalCost = totalCost.add(nightlyRate);

        // Move to the next day
        calendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    return totalCost;
}

    private BigDecimal getApplicableRate(RoomType roomType, Date date) {
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

    @Override
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
    public Reservation findReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist.");
        }
        return reservation;
    }


    @Override
    public Reservation reserveHotelRoom(Long customerId, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) 
        throws RoomTypeNotFoundException, RoomTypeUnavailableException {
    
        BigDecimal totalCost = BigDecimal.ZERO;
        Customer customer = em.find(Customer.class, customerId);
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }

        if (!isRoomTypeAvailable(roomType, checkInDate, checkOutDate)) {
            throw new RoomTypeUnavailableException("Room Type " + roomType.getName() + " is not available for the selected dates.");
        }
        
        totalCost = calculateTotalCost(roomType, checkInDate, checkOutDate).multiply(BigDecimal.valueOf(numberOfRooms));

            Reservation reservation = new Reservation();
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setTotalCost(totalCost);
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
            reservation.setCustomer(customer);
            reservation.setRoomType(roomType);
            customer.getReservations().add(reservation);

            em.persist(reservation);
            em.flush();
            Calendar currentCalendar = Calendar.getInstance();
            Calendar checkInCalendar = Calendar.getInstance();
            checkInCalendar.setTime(checkInDate);

            boolean isToday = currentCalendar.get(Calendar.YEAR) == checkInCalendar.get(Calendar.YEAR) &&
                      currentCalendar.get(Calendar.DAY_OF_YEAR) == checkInCalendar.get(Calendar.DAY_OF_YEAR);

                if (isToday && currentCalendar.get(Calendar.HOUR_OF_DAY) >= 14) { // 2:00 PM is 14:00 in 24-hour format
            allocateRoomsImmediately(reservation);
        }

        return reservation;
    }
    
    
    
    
    @Override
    public Reservation walkInReserveRoom(String firstName, String lastName, String phoneNumber, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) 
        throws RoomTypeNotFoundException, RoomTypeUnavailableException {
    
    // Check if walk-in guest already exists or create a new one
    WalkInGuest walkInGuest = findOrCreateWalkInGuest(firstName, lastName, phoneNumber);

    // Fetch the specified room type
    RoomType roomType = em.find(RoomType.class, roomTypeId);
    if (roomType == null) {
        throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
    }

    // Check availability of the requested number of rooms
    if (!isRoomTypeAvailable(roomType, checkInDate, checkOutDate, numberOfRooms)) {
        throw new RoomTypeUnavailableException("Room Type " + roomType.getName() + " does not have enough available rooms for the selected dates.");
    }

    // Calculate the total cost for all requested rooms
    BigDecimal totalCost = calculateWalkInAmount(roomType, checkInDate, checkOutDate).multiply(BigDecimal.valueOf(numberOfRooms));

    // Create a new reservation
    Reservation reservation = new Reservation();
    reservation.setCheckInDate(checkInDate);
    reservation.setCheckOutDate(checkOutDate);
    reservation.setTotalCost(totalCost);
    reservation.setStatus(Reservation.ReservationStatus.PENDING);
    reservation.setWalkInGuest(walkInGuest);
    reservation.setRoomType(roomType);
    reservation.setNumberOfRooms(numberOfRooms); // assuming the Reservation entity has a numberOfRooms field

    // Persist reservation
    em.persist(reservation);
    em.flush();

    // Determine if immediate allocation is required
    Calendar currentCalendar = Calendar.getInstance();
    Calendar checkInCalendar = Calendar.getInstance();
    checkInCalendar.setTime(checkInDate);
    boolean isToday = currentCalendar.get(Calendar.YEAR) == checkInCalendar.get(Calendar.YEAR) &&
                      currentCalendar.get(Calendar.DAY_OF_YEAR) == checkInCalendar.get(Calendar.DAY_OF_YEAR);

    if (isToday && currentCalendar.get(Calendar.HOUR_OF_DAY) >= 2) { // After 2:00 a.m.
        allocateRoomsImmediately(reservation);
    }

    return reservation;
}

// Modified method to check if requested number of rooms is available
    private boolean isRoomTypeAvailable(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms) {
        Query query = em.createQuery("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :roomType AND r.checkInDate <= :checkOutDate AND r.checkOutDate >= :checkInDate");
        query.setParameter("roomType", roomType);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        Long reservedCount = (Long) query.getSingleResult();
        int availableRooms = roomType.getCapacity() - reservedCount.intValue();

        return availableRooms >= numberOfRooms;
    }


    private WalkInGuest findOrCreateWalkInGuest(String firstName, String lastName, String phoneNumber) {
    // Search for existing walk-in guest by phone number
        Query query = em.createQuery("SELECT w FROM WalkInGuest w WHERE w.phoneNumber = :phoneNumber", WalkInGuest.class);
        query.setParameter("phoneNumber", phoneNumber);
        List<WalkInGuest> guests = query.getResultList();

        if (!guests.isEmpty()) {
            return guests.get(0); // Return existing guest if found
        } else {
        // Create a new walk-in guest
            WalkInGuest newGuest = new WalkInGuest(firstName, lastName, phoneNumber);
            em.persist(newGuest);
            em.flush();
            return newGuest;
        }
    }

    private void allocateRoomsImmediately(Reservation reservation) {
        System.out.println("Allocating rooms for reservation ID: " + reservation.getReservationId());
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        em.merge(reservation);
    }

    
    private BigDecimal calculateWalkInAmount(RoomType roomType, Date checkInDate, Date checkOutDate) {
    // Calculate the number of days between check-in and check-out dates
    long daysBetween = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);

    // Retrieve the published rate from the room type's rates
    BigDecimal publishedRate = roomType.getRoomRates().stream()
        .filter(rate -> rate.getRateType() == RateType.PUBLISHED)
        .findFirst()
        .map(RoomRate::getRatePerNight)
        .orElse(BigDecimal.ZERO);

    // Multiply the published rate by the number of days
    return publishedRate.multiply(BigDecimal.valueOf(daysBetween));
}
    
 

}


