/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomRate.RateType;
import entity.RoomType;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.*;
import javax.ejb.EJB;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author javieryeow
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;
    
    
    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createRoomRate(String name, String roomTypeName, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        RoomRate roomRate = new RoomRate();
        roomRate.setName(name);
        RoomType roomtype = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
        roomRate.setRoomType(roomtype);
        roomRate.setRateType(rateType);
        roomRate.setRatePerNight(ratePerNight);

        // Set validity period for PEAK and PROMOTION types
        if (rateType == RateType.PEAK || rateType == RateType.PROMOTION) {
            roomRate.setStartDate(startDate);
            roomRate.setEndDate(endDate);
        }

        em.persist(roomRate);
        return roomRate.getRoomRateId();
    }
    
    @Override
    public RoomRate viewRoomRateDetails(Long roomRateId) {
        return em.find(RoomRate.class, roomRateId);
    }
    
    @Override
    public void updateRoomRate(Long roomRateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if (roomRate != null && roomRate.isEnabled()) { // Only allow updates if enabled
            roomRate.setName(name);
            roomRate.setRoomType(roomType);
            roomRate.setRateType(rateType);
            roomRate.setRatePerNight(ratePerNight);
            
            if (rateType == RateType.PEAK || rateType == RateType.PROMOTION) {
                roomRate.setStartDate(startDate);
                roomRate.setEndDate(endDate);
            } else {
                roomRate.setStartDate(null);
                roomRate.setEndDate(null);
            }
            em.merge(roomRate);
        }
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if (roomRate != null) {
            if (canDeleteRoomRate(roomRateId)) { 
                em.remove(roomRate);
            } else {
                roomRate.setDisabled(); 
                em.merge(roomRate);
            }
        }
    }
    
    private boolean canDeleteRoomRate(Long roomRateId) {
        // Logic to check if room rate is not in use (e.g., no current reservations)
        // This is a placeholder for actual implementation
        return true;
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        return em.createQuery("SELECT r FROM RoomRate r", RoomRate.class).getResultList();
    }
    
     @Override
    public RoomRate findRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + roomRateId + " not found.");
        }
        return roomRate;
    }
    
}
