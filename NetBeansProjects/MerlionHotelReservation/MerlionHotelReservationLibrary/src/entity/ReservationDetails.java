/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author wkgaret
 */
@Entity
public class ReservationDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  
    private Long detailId; //different from reservatonId, this is for the specific night where rate maybe be different

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private double priceForNight;

    @ManyToOne
    @JoinColumn(name = "reservationId", nullable = false)
    private CustomerReservation reservation;

    @ManyToOne
    @JoinColumn(name = "roomTypeId", nullable = false)
    private RoomType roomType;

    public ReservationDetails() {
    }

    public ReservationDetails(LocalDate reservationDate, double priceForNight, CustomerReservation reservation, RoomType roomType) {
        this.reservationDate = reservationDate;
        this.priceForNight = priceForNight;
        this.reservation = reservation;
        this.roomType = roomType;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long DetailId) {
        this.detailId = DetailId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public double getPriceForNight() {
        return priceForNight;
    }

    public void setPriceForNight(double priceForNight) {
        this.priceForNight = priceForNight;
    }

    public CustomerReservation getReservation() {
        return reservation;
    }

    public void setReservation(CustomerReservation reservation) {
        this.reservation = reservation;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailId != null ? detailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationDetails)) {
            return false;
        }
        ReservationDetails other = (ReservationDetails) object;
        if ((this.detailId == null && other.detailId != null) || (this.detailId != null && !this.detailId.equals(other.detailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RerservationDetails[ id=" + detailId + " ]";
    }
    
}
