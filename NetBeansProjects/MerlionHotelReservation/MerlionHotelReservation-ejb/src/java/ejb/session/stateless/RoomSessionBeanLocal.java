/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author javieryeow
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createRoom(String roomNumber, RoomType roomType);

    public void updateRoom(Long roomId, String roomNumber, RoomType roomType, Room.RoomStatus status);

    public void deleteRoom(Long roomId);

    public List<Room> viewAllRooms();

    public void changeRoomAvailability(Long roomId, Room.RoomStatus newStatus);
    
}
