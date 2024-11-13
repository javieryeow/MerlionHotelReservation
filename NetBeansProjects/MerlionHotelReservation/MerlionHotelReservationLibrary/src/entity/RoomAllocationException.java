/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author javieryeow
 */
@Entity
public class RoomAllocationException implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomAllocationExceptionId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomAllocationExceptionType type;
    
    @Column(nullable = false, length = 255)
    private String issue;
    
    @OneToOne(mappedBy = "roomAllocationException", optional = true)
    private Reservation reservation;
    
    public enum RoomAllocationExceptionType {
        UPGRADE, NO_UPGRADE
    }

    public RoomAllocationException() {
    }

    public RoomAllocationException(RoomAllocationExceptionType type, String issue, Reservation reservation) {
        this.type = type;
        this.issue = issue;
        this.reservation = reservation;
    }
    
    

    public Long getRoomAllocationExceptionId() {
        return roomAllocationExceptionId;
    }

    public void setRoomAllocationExceptionId(Long roomAllocationExceptionId) {
        this.roomAllocationExceptionId = roomAllocationExceptionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomAllocationExceptionId != null ? roomAllocationExceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomAllocationExceptionId fields are not set
        if (!(object instanceof RoomAllocationException)) {
            return false;
        }
        RoomAllocationException other = (RoomAllocationException) object;
        if ((this.roomAllocationExceptionId == null && other.roomAllocationExceptionId != null) || (this.roomAllocationExceptionId != null && !this.roomAllocationExceptionId.equals(other.roomAllocationExceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomAllocationException[ id=" + roomAllocationExceptionId + " ]";
    }

    /**
     * @return the type
     */
    public RoomAllocationExceptionType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(RoomAllocationExceptionType type) {
        this.type = type;
    }

    /**
     * @return the issue
     */
    public String getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}
