/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomSessionBeanRemote {
    
    public Long createRoom(String roomNumber, String roomTypeName) throws RoomTypeNotFoundException;

    public void updateRoom(Long roomId, String roomNumber, RoomType roomType, Room.RoomStatus status) throws RoomNotFoundException;

    public void deleteRoom(Long roomId) throws RoomNotFoundException;

    public List<Room> viewAllRooms();
    
    public void changeRoomAvailability(Long roomId, Room.RoomStatus newStatus);
}
