/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerReservation;
import entity.CustomerReservation.ReservationStatus;
import entity.ReservationDetails;
import entity.RoomType;
import java.time.LocalDate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author wkgaret
 */
@Stateless
public class CreateCustomerSessionBean implements CreateCustomerSessionBeanRemote, CreateCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @Override
     public Customer registerAsCustomer(String firstName, String lastName, String email, String phoneNumber, String password) { // use case 2
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(password);
        em.persist(customer);
        em.flush(); // Ensure the ID is generated
        
        return customer;
    }
     
    @Override
        public Customer customerLogin(String email, String password) {
        try {
            Customer customer = (Customer) em.createQuery("SELECT c FROM Customer c WHERE c.email = :email")
                                             .setParameter("email", email)
                                             .getSingleResult();
            // Verify password - ensure to store passwords securely in production
            if (customer != null && customer.getPassword().equals(password)) {
                return customer; // Login successful
            }
        } catch (NoResultException e) {
            // No customer found with the provided email
            System.out.println("Customer not found for email: " + email);
        } catch (Exception e) {
            // Handle any other errors
            System.out.println("Error during login: " + e.getMessage());
        }
        return null; // Login failed
    }
     
      

        
     

    

   
}
