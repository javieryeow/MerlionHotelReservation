/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface PartnerLoginSessionBeanRemote {
    
    public Long createPartner(String username, String password);
    
    public List<Partner> viewAllPartners();
    
    public Partner partnerLogin(String username, String password) throws WrongPasswordException, InvalidPartnerLoginException;
    
    public Reservation createPartnerReservation(Long partnerId, Long customerId, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException, RoomTypeUnavailableException, PartnerNotFoundException;
    
    public Reservation viewPartnerReservation(Long partnerId, Long reservationId) throws ReservationNotFoundException;
    
    public List<Reservation> viewAllPartnerReservations(Long partnerId);
}
