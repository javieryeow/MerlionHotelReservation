/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomRate;
import entity.RoomRate.RateType;
import entity.RoomType;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

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
    public Long createRoomRate(String name, String roomTypeName, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomTypeNotFoundException {
        RoomRate roomRate = new RoomRate();
        roomRate.setName(name);
        RoomType roomtype;
        try {
            roomtype = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
            roomRate.setRoomType(roomtype);
            roomtype.getRoomRates().add(roomRate);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type Not Found.");
        }
        roomRate.setRateType(rateType);
        roomRate.setRatePerNight(ratePerNight);

        // Set validity period for PEAK and PROMOTION types
        if (rateType == RateType.PEAK || rateType == RateType.PROMOTION) {
            roomRate.setStartDate(startDate);
            roomRate.setEndDate(endDate);
        }

        em.persist(roomRate);
        em.flush();
        return roomRate.getRoomRateId();
    }
    
    @Override
    public RoomRate viewRoomRateDetails(Long roomRateId) {
        return em.find(RoomRate.class, roomRateId);
    }
    
    @Override
    public void updateRoomRate(Long roomRateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException {
        try {
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
        } catch (NoResultException ex) {
            throw new RoomRateNotFoundException("Room Rate Not Found");
        } 
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException {
        try {
            RoomRate roomRate = em.find(RoomRate.class, roomRateId);
            if (roomRate != null) {
                List<Reservation> reservations = roomRate.getReservations();
                if (reservations.size() > 0) { 
                    roomRate.setDisabled();
                } else {
                    em.remove(roomRate); 
                    em.flush();
                }
            }
        } catch (NoResultException ex) {
            throw new RoomRateNotFoundException("Room Rate Not Found!");
        }
        
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
    
    @Override
    public BigDecimal getReservationRate(RoomType roomType, Date date) throws RoomRateNotFoundException {
        // Query to retrieve all active room rates for the specified room type and date
        List<RoomRate> roomRates = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomType = :roomType AND :date BETWEEN r.startDate AND r.endDate AND r.enabled = true", RoomRate.class)
                                      .setParameter("roomType", roomType)
                                      .setParameter("date", date)
                                      .getResultList();
        List<RoomRate> normalRoomRates = em.createQuery("SELECT r FROM RoomRate r WHERE r.roomType = :roomType AND r.enabled = true AND r.rateType = :rateType", RoomRate.class)
                                        .setParameter("roomType", roomType)
                                        .setParameter("rateType", RoomRate.RateType.NORMAL)
                                        .getResultList();
        for (RoomRate roomRate : normalRoomRates) {
            roomRates.add(roomRate);
        }

        BigDecimal applicableRate = null;

        // Loop through the rates and select the most applicable one based on the type
        for (RoomRate rate : roomRates) {
            if (rate.getRateType() == RateType.PROMOTION) {
                applicableRate = rate.getRatePerNight();
                break;
            }
        }
        if (applicableRate == null) {
            for (RoomRate rate : roomRates) {
               if (rate.getRateType() == RateType.PEAK) {
                applicableRate = rate.getRatePerNight();
                break;
                } 
            }
        }
        if (applicableRate == null) {
            for (RoomRate rate : roomRates) {
               if (rate.getRateType() == RateType.NORMAL) {
                applicableRate = rate.getRatePerNight();
                break;
                } 
            }
        }

        if (applicableRate == null){
            throw new RoomRateNotFoundException("No applicable room rate found for the specified date and room type.");
        }

        return applicableRate;
    }
    
}
