/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    
    public Long createRoomType(String name, String description, int size, int bed, int capacity, String amenities, String higherRoomTypeName) throws RoomTypeNotFoundException;

    public RoomType viewRoomType(Long roomTypeId);

    public void updateRoomType(Long roomTypeId, String name, String description, int size, int bed, int capacity, String amenities);
    
    public void deleteRoomType(Long roomTypeId);
    
    public List<RoomType> viewAllRoomTypes();
    
    public RoomType findRoomTypeById(Long roomTypeId);
    
    public RoomType findRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException;
    
}
