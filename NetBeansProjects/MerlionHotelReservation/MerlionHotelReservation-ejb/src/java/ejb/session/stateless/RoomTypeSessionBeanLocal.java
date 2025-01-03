/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createRoomType(String name, String description, int size, int bed, int capacity, String amenities, String higherRoomTypeName) throws RoomTypeNotFoundException;

    public RoomType viewRoomType(Long roomTypeId);

    public void updateRoomType(Long roomTypeId, String name, String description, int size, int bed, int capacity, String amenities);

    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException;

    public List<RoomType> viewAllRoomTypes();

    public RoomType findRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException;

    public RoomType findRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException;
    
}
