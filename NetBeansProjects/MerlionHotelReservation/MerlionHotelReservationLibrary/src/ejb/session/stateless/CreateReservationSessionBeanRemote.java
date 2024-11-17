/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;

/**
 *
 * @author wkgaret
 */
@Remote
public interface CreateReservationSessionBeanRemote {
    
     public List<RoomType> searchAvailableRooms(Date checkInDate, Date checkOutDate);

//    // Calculate total cost based on room type and date range
//    public BigDecimal calculateTotalCost(RoomType roomType, Date checkInDate, Date checkOutDate);

    // View a specific customer reservation
    public Reservation viewCustomerReservation(Long customerId, Long reservationId);

    // Reserve a hotel room with a list of room type IDs and date range

   public Reservation reserveHotelRoom(Long customerId, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) 
        throws RoomTypeNotFoundException, RoomTypeUnavailableException;

    // View all reservations of a specific customer
    public List<Reservation> viewAllReservations(Long customerId);

    // i think dont need
    public void updateReservationStatus(Long reservationId, Reservation.ReservationStatus newStatus) throws Exception;
    
    public Reservation walkInReserveRoom(String firstName, String lastName, String phoneNumber, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException, RoomTypeUnavailableException;

     public Reservation findReservationById(Long reservationId) throws ReservationNotFoundException;
     
     public BigDecimal calculateTotalCostForOnlineReservation(RoomType roomType, Date checkInDate, Date checkOutDate, int numberOfRooms);
 
}
