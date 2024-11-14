/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerNotFoundException;
import util.exception.WrongPasswordException;

/**
 *
 * @author wkgaret
 */
@Stateless
public class CreateCustomerSessionBean implements CreateCustomerSessionBeanRemote, CreateCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @Override
    public Customer registerAsCustomer(String firstName, String lastName, String email, String phoneNumber, String password) throws CustomerAlreadyExistException { // use case 2
        try {
            Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :email");
            query.setParameter("email", email);
            Customer existingCustomer = (Customer) query.getSingleResult();
            throw new CustomerAlreadyExistException("A customer with this email already exists.");
        }
        catch (NoResultException ex) {
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setPassword(password);
            em.persist(customer);
            em.flush(); // Ensure the ID is generated

            return customer; // Return the created customer
        }   
    }
     
    @Override
    public Customer customerLogin(String email, String password) throws CustomerNotFoundException, WrongPasswordException {
    // Retrieve the customer based on email
    TypedQuery<Customer> query = em.createQuery(
            "SELECT c FROM Customer c WHERE c.email = :inEmail", 
            Customer.class);
        query.setParameter("inEmail", email);
        
        Customer customer = null;
    try {
        customer = query.getSingleResult();  
    } catch (NoResultException ex) {
        throw new CustomerNotFoundException("Customer not found for email: " + email);  
    }
    
    if (!customer.getPassword().equals(password)) {
        throw new WrongPasswordException("Incorrect password for email: " + email);
    }
    return customer;
}

     
      

        
     

    

   
}
