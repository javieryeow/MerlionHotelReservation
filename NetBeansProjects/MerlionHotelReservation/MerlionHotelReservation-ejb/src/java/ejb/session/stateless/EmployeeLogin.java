/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.exception.InvalidEmployeeLoginException;

/**
 *
 * @author javieryeow
 */
@Stateless
public class EmployeeLogin implements EmployeeLoginRemote, EmployeeLoginLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public Employee login(String username, String password) throws InvalidEmployeeLoginException {
        TypedQuery<Employee> query = em.createQuery(
        "SELECT e from Employee e WHERE e.username = :inUsername AND e.password = :inPassword", Employee.class);
        query.setParameter("inUsername", username);
        query.setParameter("inPassword", password);
        Employee employee = null;
        try {
            employee = query.getSingleResult();  
        } catch (NoResultException e) {
            throw new InvalidEmployeeLoginException("Login failed! Please try again.");  
        }
        return employee;
    } 
}
