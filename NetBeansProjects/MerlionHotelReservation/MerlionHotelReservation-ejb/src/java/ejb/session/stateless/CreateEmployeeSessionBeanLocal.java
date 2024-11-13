/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author javieryeow
 */
@Local
public interface CreateEmployeeSessionBeanLocal {

    public Long createOperationManager(String username, String password);

    public Long createSalesManager(String username, String password);

    public Long createGRO(String username, String password);

    public Long createSystemAdmin(String username, String password);

    public List<Employee> viewAllEmployees();

    public Employee findEmployeeById(Long employeeId);
    
}
