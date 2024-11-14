/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import java.util.Date;
import javax.ejb.Remote;
import util.exception.InvalidStatusTransitionException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author javieryeow
 */
@Remote
public interface AllocatingRoomSessionBeanRemote {
    public void allocateRoomForReservation(Date checkInDate);
}
