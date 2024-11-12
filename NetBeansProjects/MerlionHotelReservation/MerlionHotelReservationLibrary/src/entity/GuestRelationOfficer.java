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
public class GuestRelationOfficer extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public GuestRelationOfficer() {
        super();
    }
    
    public GuestRelationOfficer(String username, String password) {
        super(username, password, role.GUEST_RELATIONS_OFFICER);
    }

    @Override
    public String toString() {
        return "entity.GuestRelationOfficer[ id=" + super.employeeId + " ]";
    }
    
}
