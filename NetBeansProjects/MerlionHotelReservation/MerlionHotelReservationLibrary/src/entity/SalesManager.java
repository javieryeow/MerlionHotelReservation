/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author javieryeow
 */
@Entity
public class SalesManager extends Employee implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public SalesManager() {
        super();
    }
    
    public SalesManager(String username, String password) {
        super(username, password, role.SALES_MANAGER);
    }
    
    @Override
    public String toString() {
        return "entity.SalesManager[ id=" + super.employeeId + " ]";
    }
    
}
