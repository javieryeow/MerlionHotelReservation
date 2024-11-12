/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;
import java.util.*;

/**
 *
 * @author javieryeow
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RateType rateType;
    
    @Column(nullable = false)
    private BigDecimal ratePerNight;
    
    private LocalDate startDate; // Only for PEAK and PROMOTION
    
    private LocalDate endDate;   // Only for PEAK and PROMOTION
    
    @Column(nullable = false)
    private boolean enabled = true;
    
    public enum RateType {
    PUBLISHED, NORMAL, PEAK, PROMOTION;
    }

    public RoomRate() {
    }

    public RoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.roomType = roomType;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    

    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the rateType
     */
    public RateType getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the ratePerNight
     */
    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @param enabled the enabled to set
     */
    public void setEnabled() {
        this.enabled = true;
    }
    
    public void setDisabled() {
        this.enabled = false;
    }
    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
}
