/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomAllocationException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author javieryeow
 */
@Remote
public interface CreateRoomAllocationExceptionSessionBeanRemote {
    public Long createRoomAllocationException(RoomAllocationException.RoomAllocationExceptionType type, String issue, Reservation reservation);
    
    public List<RoomAllocationException> viewAllRoomAllocationException();
}
