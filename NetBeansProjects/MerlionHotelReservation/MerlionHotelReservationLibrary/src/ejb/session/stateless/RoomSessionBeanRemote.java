/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomSessionBeanRemote {
    
    public Long createRoom(Room room);

    public void updateRoom(Room room);

    public void deleteRoom(Long roomId);

    public List<Room> viewAllRooms();
    
    public void changeRoomAvailability(Long roomId, Room.RoomStatus newStatus);
}
