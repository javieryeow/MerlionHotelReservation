/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    
    public Long createRoomType(RoomType roomType);

    public RoomType viewRoomType(Long roomTypeId);

    public void updateRoomType(RoomType roomType);
    
    public void deleteRoomType(Long roomTypeId);
    
    public List<RoomType> viewAllRoomTypes();
    
}
