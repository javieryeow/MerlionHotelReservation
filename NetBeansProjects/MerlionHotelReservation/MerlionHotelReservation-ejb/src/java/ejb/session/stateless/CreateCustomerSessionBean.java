/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
            // Check if customer already exists
            Customer existingCustomer = em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                                          .setParameter("email", email)
                                          .getResultStream()
                                          .findFirst()
                                          .orElse(null);

            if (existingCustomer != null) {
                // If customer already exists, throw the exception
                throw new CustomerAlreadyExistException("A customer with this email already exists.");
            }

            // Create a new customer
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customer.setPassword(password);

            // Persist the new customer to the database
            em.persist(customer);
            em.flush(); // Ensure the ID is generated

            return customer; // Return the created customer
        }
        catch (CustomerAlreadyExistException ex) {
            // Log or handle the exception if needed
            System.out.println(ex.getMessage());
            throw ex; // Re-throw the exception so it can be handled at a higher level
        }
    }
     
    @Override
       public Customer customerLogin(String email, String password) throws CustomerNotFoundException, WrongPasswordException {
    // Retrieve the customer based on email
    Customer customer = (Customer) em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                                     .setParameter("email", email)
                                     .getSingleResult();
    
    // Check if the customer was found
    if (customer == null) {
        throw new CustomerNotFoundException("Customer not found for email: " + email);
    }
    
    // Verify password - ensure to store passwords securely in production
    if (!customer.getPassword().equals(password)) {
        throw new WrongPasswordException("Incorrect password for email: " + email);
    }
    
    return customer; // Login successful
}

     
      

        
     

    

   
}
