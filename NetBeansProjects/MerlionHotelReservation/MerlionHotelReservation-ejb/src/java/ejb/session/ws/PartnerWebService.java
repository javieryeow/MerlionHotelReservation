/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerLoginSessionBeanLocal;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author javieryeow
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerLoginSessionBeanLocal partnerLoginSessionBean;
    
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }


}
