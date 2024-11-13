/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import entity.Employee;
import entity.GuestRelationOfficer;
import entity.OperationManager;
import entity.SalesManager;
import entity.SystemAdministrator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author javieryeow
 */
@Stateless
public class CreateEmployeeSessionBean implements CreateEmployeeSessionBeanRemote, CreateEmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createOperationManager(String username, String password) {
        OperationManager om = new OperationManager(username, password);
        em.persist(om);
        em.flush();
        return om.getEmployeeId();
    }
    
    @Override
    public Long createSalesManager(String username, String password) {
        SalesManager sm = new SalesManager(username, password);
        em.persist(sm);
        em.flush();
        return sm.getEmployeeId();
    }
    
    @Override
    public Long createGRO(String username, String password) {
        GuestRelationOfficer gro = new GuestRelationOfficer(username, password);
        em.persist(gro);
        em.flush();
        return gro.getEmployeeId();
    }
    
    @Override
    public Long createSystemAdmin(String username, String password) {
        SystemAdministrator admin = new SystemAdministrator(username, password);
        em.persist(admin);
        em.flush();
        return admin.getEmployeeId();
    }
    
    @Override
    public List<Employee> viewAllEmployees() {
        Query query = em.createQuery("Select e from Employee e");
        
        return query.getResultList();
    }
    
    @Override
    public Employee findEmployeeById(Long employeeId) {
        return em.find(Employee.class, employeeId);
    }
}
