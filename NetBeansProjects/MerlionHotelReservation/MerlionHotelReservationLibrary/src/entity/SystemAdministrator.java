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
@DiscriminatorValue("SystemAdmin")
public class SystemAdministrator extends Employee {

     public SystemAdministrator() {
        super();
    }
    
    public SystemAdministrator(String username, String password) {
        super(username, password, role.SYSTEM_ADMIN);
    }

    @Override
    public String toString() {
        return "entity.SystemAdministrator[ id=" + super.employeeId + " ]";
    } 
}
