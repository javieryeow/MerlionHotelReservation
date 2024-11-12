/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
/**
 *
 * @author javieryeow
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long employeeId;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
     
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private role status;
    
    public enum role {
    OPERATION_MANAGER, SYSTEM_ADMIN, SALES_MANAGER, GUEST_RELATIONS_OFFICER;
    }

    public Employee() {
    }

    public Employee(String username, String password, role status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return the status
     */
    public role getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(role status) {
        this.status = status;
    }
}
