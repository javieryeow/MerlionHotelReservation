/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Room;
import entity.Room.RoomStatus;
import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.NoResultException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;
    
    
    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createRoom(String roomNumber, String roomTypeName) throws RoomTypeNotFoundException {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        RoomType rt;
        try {
            rt = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
            room.setRoomType(rt);
            rt.getRooms().add(room);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("RoomType Not found.");
        }
        em.persist(room);
        em.flush();
        return room.getRoomId();
    }
    
    @Override
    public void updateRoom(Long roomId, String roomNumber, RoomType roomType, RoomStatus status) throws RoomNotFoundException {
        try {
            Room room = em.find(Room.class, roomId);
            if (room != null) {
                room.setRoomNumber(roomNumber);
                room.setRoomType(roomType);
                room.setStatus(status);
            }
        } catch (NoResultException ex) {
            throw new RoomNotFoundException("Room Not Found!");
        }
    }

    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException {
        try {
            Room room = em.find(Room.class, roomId);
            if (room != null) {
                List<Reservation> reservations = room.getReservations();
                if (reservations.size() > 0) {
                    room.setDisabled();
                } else {
                    em.remove(room);
                    em.flush();
                }
            }
        } catch (NoResultException ex) {
            throw new RoomNotFoundException("Room Not Found!");
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

}
