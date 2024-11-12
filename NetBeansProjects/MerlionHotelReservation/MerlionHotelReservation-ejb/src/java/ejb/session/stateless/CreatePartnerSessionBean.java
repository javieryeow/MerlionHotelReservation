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
/**
 *
 * @author javieryeow
 */
@Stateless
public class CreatePartnerSessionBean implements CreatePartnerSessionBeanRemote, CreatePartnerSessionBeanLocal {

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
    
    
    
    
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
