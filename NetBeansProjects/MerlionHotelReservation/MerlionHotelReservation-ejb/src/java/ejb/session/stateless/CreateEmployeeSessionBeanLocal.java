/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeAlreadyExistsException;

/**
 *
 * @author javieryeow
 */
@Local
public interface CreateEmployeeSessionBeanLocal {

    public Long createOperationManager(String username, String password) throws EmployeeAlreadyExistsException;

    public Long createSalesManager(String username, String password) throws EmployeeAlreadyExistsException;

    public Long createGRO(String username, String password) throws EmployeeAlreadyExistsException;

    public Long createSystemAdmin(String username, String password) throws EmployeeAlreadyExistsException;

    public List<Employee> viewAllEmployees();

    public Employee findEmployeeById(Long employeeId);
    
}
