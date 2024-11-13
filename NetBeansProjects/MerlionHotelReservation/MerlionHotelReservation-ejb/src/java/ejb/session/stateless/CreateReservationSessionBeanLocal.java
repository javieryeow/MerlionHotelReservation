/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CreateReservationSessionBeanLocal {

    // Search for available rooms within a date range
    public List<RoomType> searchAvailableRooms(Date checkInDate, Date checkOutDate);

    // Calculate total cost based on room type and date range
    public BigDecimal calculateTotalCost(RoomType roomType, Date checkInDate, Date checkOutDate);

    // View a specific customer reservation
    public Reservation viewCustomerReservation(Long customerId, Long reservationId);

    // Reserve a hotel room with a list of room type IDs and date range
    public Reservation reserveHotelRoom(Customer customer, List<Long> roomTypeIds, Date checkInDate, Date checkOutDate) throws Exception;

    // View all reservations of a specific customer
    public List<Reservation> viewAllReservations(Long customerId);

    // Update the reservation status
    public void updateReservationStatus(Long reservationId, Reservation.ReservationStatus newStatus) throws Exception;
}
