/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createRoomType(String name, String description, int size, int bed, int capacity, String amenities, String higherRoomTypeName) throws RoomTypeNotFoundException {
        RoomType rt = new RoomType();
        rt.setName(name);
        rt.setDescription(description);
        rt.setSize(size);
        rt.setBed(bed);
        rt.setCapacity(capacity);
        rt.setAmenities(amenities);
        try {
            if (!higherRoomTypeName.equals("None")) {
            RoomType higherRoomType = findRoomTypeByName(higherRoomTypeName);
            rt.setHigherRoomType(higherRoomType);
            }
        } 
        catch (RoomTypeNotFoundException ex) {
            throw ex;  
        }
        em.persist(rt);
        em.flush();
        return rt.getRoomTypeId();
     }
 

    @Override
    public RoomType viewRoomType(Long roomTypeId) {
        return em.find(RoomType.class, roomTypeId);
    }

    @Override
    public void updateRoomType(Long roomTypeId, String name, String description, int size, int bed, int capacity, String amenities) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType != null) {
            roomType.setName(name);
            roomType.setDescription(description);
            roomType.setSize(size);
            roomType.setBed(bed);
            roomType.setCapacity(capacity);
            roomType.setAmenities(amenities);
        }
    }
    
    @Override
    public void deleteRoomType(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType != null) {
            roomType.setDisabled();
            em.merge(roomType);
        }
    }
    
    @Override
    public List<RoomType> viewAllRoomTypes() {
        return em.createQuery("SELECT r FROM RoomType r", RoomType.class).getResultList();
    }
    
    @Override
    public RoomType findRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        try {
            return em.find(RoomType.class, roomTypeId);
        }
        catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("Room Type with ID: " + roomTypeId + " does not exist");
        }
    }
    
    @Override
    public RoomType findRoomTypeByName(String roomTypeName) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt from RoomType rt WHERE rt.name = :inName");
        query.setParameter("inName", roomTypeName);
        RoomType roomType;
        
        try {
            roomType = (RoomType) query.getSingleResult();
            return roomType; 
        } catch (NoResultException ex) {
            throw new RoomTypeNotFoundException("Room Type " + roomTypeName + " does not exist!");
        }
    }
}
