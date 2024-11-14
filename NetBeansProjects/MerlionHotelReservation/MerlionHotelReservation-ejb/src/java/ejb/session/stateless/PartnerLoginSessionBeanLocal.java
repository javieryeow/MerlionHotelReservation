/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidPartnerLoginException;
import util.exception.WrongPasswordException;

/**
 *
 * @author javieryeow
 */
@Local
public interface PartnerLoginSessionBeanLocal {

    public Long createPartner(String username, String password);

    public List<Partner> viewAllPartners();

    public Partner partnerLogin(String username, String password) throws WrongPasswordException, InvalidPartnerLoginException;
    
}