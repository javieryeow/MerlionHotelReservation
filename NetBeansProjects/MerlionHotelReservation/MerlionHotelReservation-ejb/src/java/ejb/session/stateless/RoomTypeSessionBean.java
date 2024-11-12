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
import javax.persistence.Query;

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createRoomType(String name, String description, int size, int bed, int capacity, String amenities) {
        RoomType rt = new RoomType();
        rt.setName(name);
        rt.setDescription(description);
        rt.setSize(size);
        rt.setBed(bed);
        rt.setCapacity(capacity);
        rt.setAmenities(amenities);
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
    public RoomType findRoomTypeById(Long roomTypeId) {
        return em.find(RoomType.class, roomTypeId);
    }
    
    @Override
    public RoomType findRoomTypeByName(String roomTypeName) {
        Query query = em.createQuery("SELECT rt from RoomType rt WHERE rt.name := inName");
        query.setParameter(":inName", roomTypeName);
        RoomType roomtype = (RoomType) query.getSingleResult();
        return roomtype;
    }
}
