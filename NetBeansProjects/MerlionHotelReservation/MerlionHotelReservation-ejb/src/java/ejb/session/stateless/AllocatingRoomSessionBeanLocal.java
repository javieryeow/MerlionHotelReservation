/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import java.util.Date;
import javax.ejb.Local;
import util.exception.InvalidStatusTransitionException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author javieryeow
 */
@Local
public interface AllocatingRoomSessionBeanLocal {

    public void allocateRoomForReservation(Date checkInDate);
    
}
