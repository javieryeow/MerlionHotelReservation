/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.*;
import java.util.*;
import util.exception.InvalidPartnerLoginException;
import util.exception.WrongPasswordException;
/**
 *
 * @author javieryeow
 */
@Stateless
public class PartnerLoginSessionBean implements PartnerLoginSessionBeanRemote, PartnerLoginSessionBeanLocal {

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
}
