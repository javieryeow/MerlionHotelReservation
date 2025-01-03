/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.exception.InvalidEmployeeLoginException;

/**
 *
 * @author javieryeow
 */
@Local
public interface EmployeeLoginLocal {

    public Employee login(String username, String password) throws InvalidEmployeeLoginException;
    
}
