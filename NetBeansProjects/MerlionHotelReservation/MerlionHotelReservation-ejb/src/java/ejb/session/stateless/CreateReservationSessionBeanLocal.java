/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerReservation;
import entity.ReservationDetails;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wkgaret
 */
@Local
public interface CreateReservationSessionBeanLocal {

    public List<RoomType> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);

    public CustomerReservation createReservation(Customer customer, LocalDate checkInDate, LocalDate checkOutDate, double totalCost);

    public BigDecimal calculateTotalCost(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate);

    public ReservationDetails addReservationDetail(CustomerReservation reservation, LocalDate reservationDate, double priceForNight, RoomType roomType);

    public void updateReservationStatus(Long reservationId, CustomerReservation.ReservationStatus newStatus) throws Exception;

    public CustomerReservation viewCustomerReservation(Long customerId, Long reservationId);

    public List<CustomerReservation> viewAllReservations();

    public CustomerReservation reserveHotelRoom(Customer customer, List<Long> roomTypeIds, LocalDate checkInDate, LocalDate checkOutDate) throws Exception;
    
}
