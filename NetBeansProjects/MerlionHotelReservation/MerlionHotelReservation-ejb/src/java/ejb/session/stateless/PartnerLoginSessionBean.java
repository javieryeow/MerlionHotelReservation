/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.*;
import java.util.*;
import javax.ejb.EJB;
import util.exception.InvalidPartnerLoginException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.exception.WrongPasswordException;
/**
 *
 * @author javieryeow
 */
@Stateless
public class PartnerLoginSessionBean implements PartnerLoginSessionBeanRemote, PartnerLoginSessionBeanLocal {

    @EJB
    private CreateReservationSessionBeanLocal createReservationSessionBean;
    
    

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @Override
    public Long createPartner(String username, String password) {
        Partner partner = new Partner();
        partner.setUsername(username);
        partner.setPassword(password);
        em.persist(partner);
        em.flush();
        return partner.getPartnerId();
    }
    
    @Override
    public List<Partner> viewAllPartners() {
        Query query = em.createQuery("Select p from Partner p");
        
        return query.getResultList();
    }
    
    public Partner findPartnerById(Long partnerId) throws PartnerNotFoundException {
        try {
            return em.find(Partner.class, partnerId);
        } catch (NoResultException ex) {
            throw new PartnerNotFoundException("Partner does not exist! Please try again.");
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws WrongPasswordException, InvalidPartnerLoginException {
        try {
            TypedQuery<Partner> query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :username", Partner.class);
            query.setParameter("username", username);
            Partner partner = query.getSingleResult();
            
            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new WrongPasswordException("Wrong password entered for Username: " + username + ". Please try again.");
            }
        } catch (NoResultException ex) {
            throw new InvalidPartnerLoginException("Invalid login credentials! Please try again.");
        }
    }
    
    @Override
    public Reservation createPartnerReservation(Long partnerId, Long customerId, Long roomTypeId, int numberOfRooms, Date checkInDate, Date checkOutDate) throws RoomTypeNotFoundException, RoomTypeUnavailableException, PartnerNotFoundException {
        Reservation reservation = createReservationSessionBean.reserveHotelRoom(customerId, roomTypeId, numberOfRooms, checkInDate, checkOutDate);
        Partner partner = findPartnerById(partnerId);
        if (partner == null) {
            throw new PartnerNotFoundException("Partner does not exist! Please try again.");
        }
        reservation.setPartner(partner);
        partner.getReservations().add(reservation);
        em.merge(reservation);
        em.flush();
        return reservation;
    }
    
    @Override
    public Reservation viewPartnerReservation(Long partnerId, Long reservationId) throws ReservationNotFoundException {
        Query query = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId AND r.reservationId = :reservationId", 
            Reservation.class
        );
        query.setParameter("partnerId", partnerId);
        query.setParameter("reservationId", reservationId);

        try {
            return (Reservation) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new ReservationNotFoundException("Reservation Not Found! Please try again.");
        }
    }
    
    @Override
    public List<Reservation> viewAllPartnerReservations(Long partnerId) {
        Query query = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId", 
            Reservation.class
        );
        query.setParameter("partnerId", partnerId);

        return query.getResultList();
    }
}
