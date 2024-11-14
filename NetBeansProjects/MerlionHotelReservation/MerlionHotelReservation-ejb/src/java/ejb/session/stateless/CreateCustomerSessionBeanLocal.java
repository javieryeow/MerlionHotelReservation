/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerNotFoundException;
import util.exception.WrongPasswordException;

/**
 *
 * @author wkgaret
 */
@Local
public interface CreateCustomerSessionBeanLocal {
    
    public Customer customerLogin(String email, String password) throws CustomerNotFoundException, WrongPasswordException;

    public Customer registerAsCustomer(String firstName, String lastName, String email, String phoneNumber, String password) throws CustomerAlreadyExistException;
    
}
