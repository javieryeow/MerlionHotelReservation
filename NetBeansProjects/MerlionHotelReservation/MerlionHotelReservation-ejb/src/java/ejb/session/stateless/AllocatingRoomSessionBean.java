/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.*;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author javieryeow
 */
@Stateless
public class AllocatingRoomSessionBean implements AllocatingRoomSessionBeanRemote, AllocatingRoomSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    public Room allocateRoomForReservation(Date checkInDate) {
        Query query = em.createQuery("SELECT res FROM Reservation res WHERE res.checkinDate = :inCheckinDate");
        query.setParameter("inCheckinDate", checkInDate);
        List<Reservation> reservations = (List<Reservation>) query.getResultList();
        for (Reservation r: reservations) {
            if(!hasAllocated(r)) {
                
            }
        }
    }
    
    private boolean hasAllocated(Reservation reservation) {
        List<Room> rooms = reservation.getRooms();
        if (rooms.size() != 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
