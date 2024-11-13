/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author javieryeow
 */
@Entity
public class WalkInGuest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkInGuestId;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    
    @OneToMany(mappedBy = "walkInGuest", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public WalkInGuest() {
        this.reservations = new ArrayList<Reservation>();
    }

    public WalkInGuest(String firstName, String lastName, String phoneNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    public Long getWalkInGuestId() {
        return walkInGuestId;
    }

    public void setWalkInGuestId(Long walkInGuestId) {
        this.walkInGuestId = walkInGuestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (walkInGuestId != null ? walkInGuestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the walkInGuestId fields are not set
        if (!(object instanceof WalkInGuest)) {
            return false;
        }
        WalkInGuest other = (WalkInGuest) object;
        if ((this.walkInGuestId == null && other.walkInGuestId != null) || (this.walkInGuestId != null && !this.walkInGuestId.equals(other.walkInGuestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.WalkInGuest[ id=" + walkInGuestId + " ]";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
