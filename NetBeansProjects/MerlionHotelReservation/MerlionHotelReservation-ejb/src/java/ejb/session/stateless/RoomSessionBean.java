/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.Room.RoomStatus;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    public Long createRoom(Room room) {
        em.persist(room);
        em.flush();
        return room.getRoomId();
    }
    
    public void updateRoom(Room room) {
        em.merge(room);
    }

    public void deleteRoom(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            room.setDisabled();
            em.merge(room);
        }
    }

    @Override
    public List<Room> viewAllRooms() {
        return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
    }
    
    @Override
    public void changeRoomAvailability(Long roomId, RoomStatus newStatus) {
        Room room = em.find(Room.class, roomId);
        
        if (room != null && room.isEnabled()) { // Ensure the room is enabled
            room.setStatus(newStatus);
            em.merge(room);
        }
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
