/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.CreateReservationSessionBeanLocal;
import ejb.session.stateless.PartnerLoginSessionBeanLocal;
import entity.Partner;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidPartnerLoginException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.exception.WrongPasswordException;

/**
 *
 * @author javieryeow
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @EJB
    private CreateReservationSessionBeanLocal createReservationSessionBean;

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerLoginSessionBeanLocal partnerLoginSessionBean;
    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws WrongPasswordException, InvalidPartnerLoginException {
        Partner partner = partnerLoginSessionBean.partnerLogin(username, password);
        em.detach(partner);
        for (Reservation reservation : partner.getReservations()) {
            em.detach(reservation);
            reservation.setPartner(null);
            reservation.setCustomer(null);
            reservation.setWalkInGuest(null);
            reservation.setRoomAllocationException(null);
            reservation.getRooms().clear();
        }
        return partner;
    }
    
    @WebMethod(operationName = "partnerSearchAvailableRooms")
    public List<RoomType> partnerSearchAvailableRooms(@WebParam(name = "checkInDate") String checkInDate, @WebParam(name = "checkOutDate") String checkOutDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date checkIn = dateFormat.parse(checkInDate);
        Date checkOut = dateFormat.parse(checkOutDate);
        List<RoomType> availableRooms = createReservationSessionBean.searchAvailableRooms(checkIn, checkOut);
        for (RoomType roomType : availableRooms) {
            em.detach(roomType);
            roomType.getRoomRates().clear();
            for (RoomRate roomRate: roomType.getRoomRates()){
                em.detach(roomRate);
                roomRate.setRoomType(null);
            }
            roomType.getRooms().clear();
            for (Room room : roomType.getRooms()) {
                em.detach(room);
                room.setRoomType(null);
            }
        }
        return availableRooms;
    }
    
    @WebMethod(operationName = "partnerReserveRoom")
    public Reservation partnerReserveRoom(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "customerId") Long customerId, @WebParam(name = "roomTypeId") Long roomTypeId, @WebParam(name = "numberOfRooms") int numberOfRooms, @WebParam(name = "checkInDate") String checkInDate, @WebParam(name = "checkOutDate") String checkOutDate) throws RoomTypeNotFoundException, RoomTypeUnavailableException, PartnerNotFoundException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date checkIn = dateFormat.parse(checkInDate);
        Date checkOut = dateFormat.parse(checkOutDate);
        Reservation reservation = partnerLoginSessionBean.createPartnerReservation(partnerId, customerId, roomTypeId, numberOfRooms, checkIn, checkOut);
        em.detach(reservation);
        reservation.setCustomer(null);
        reservation.setRoomAllocationException(null);
        reservation.setPartner(null);
        reservation.getRooms().clear();
        reservation.setRoomType(null);
        return reservation;
    }
    
    @WebMethod(operationName = "viewPartnerReservation") 
    public Reservation viewPartnerReservation(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = partnerLoginSessionBean.viewPartnerReservation(partnerId, reservationId);
        em.detach(reservation);
        reservation.setCustomer(null);
        reservation.setRoomAllocationException(null);
        reservation.setPartner(null);
        reservation.getRooms().clear();
        reservation.setRoomType(null);
        return reservation;
    }
    
    @WebMethod(operationName = "viewAllPartnerReservations") 
    public List<Reservation> viewAllPartnerReservations(@WebParam(name = "partnerId") Long partnerId) {
        List<Reservation> reservations = partnerLoginSessionBean.viewAllPartnerReservations(partnerId);
        for (Reservation reservation : reservations) {
            em.detach(reservation);
            reservation.setCustomer(null);
            reservation.setRoomAllocationException(null);
            reservation.setPartner(null);
            reservation.getRooms().clear();
            reservation.setRoomType(null);
        }
        return reservations;
    }
}
