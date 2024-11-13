/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomAllocationException;
import entity.RoomAllocationException.RoomAllocationExceptionType;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author javieryeow
 */
@Stateless
public class CreateRoomAllocationExceptionSessionBean implements CreateRoomAllocationExceptionSessionBeanRemote, CreateRoomAllocationExceptionSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createRoomAllocationException(RoomAllocationExceptionType type, String issue, Reservation reservation) {
        RoomAllocationException roomAllocationException = new RoomAllocationException();
        roomAllocationException.setType(type);
        roomAllocationException.setIssue(issue);
        roomAllocationException.setReservation(reservation);
        em.persist(roomAllocationException);
        em.flush();
        return roomAllocationException.getRoomAllocationExceptionId();
    }
    
    
    @Override
    public List<RoomAllocationException> viewAllRoomAllocationException() {
        return em.createQuery("SELECT r FROM RoomAllocationException r", RoomAllocationException.class).getResultList();
    } 
}
