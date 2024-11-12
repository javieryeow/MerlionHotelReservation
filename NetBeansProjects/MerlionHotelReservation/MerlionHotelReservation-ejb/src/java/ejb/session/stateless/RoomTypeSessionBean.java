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

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createRoomType(RoomType roomType) {
        em.persist(roomType);
        em.flush();
        return roomType.getRoomTypeId();
    }

    @Override
    public RoomType viewRoomType(Long roomTypeId) {
        return em.find(RoomType.class, roomTypeId);
    }

    @Override
    public void updateRoomType(RoomType roomType) {
        em.merge(roomType);
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
}
