/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author javieryeow
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createRoom(Room room);

    public void updateRoom(Room room);

    public void deleteRoom(Long roomId);

    public List<Room> viewAllRooms();

    public void changeRoomAvailability(Long roomId, Room.RoomStatus newStatus);
    
}
