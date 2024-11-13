/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkOutDate;
    
    @Column(nullable = false)
    private BigDecimal totalCost;
    
    @Column(nullable = false)
    private int numberOfRooms;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reservationId")
    private List<RoomRate> roomRates;
    
    @ManyToMany
    private List<Room> rooms;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "customerId", nullable = true)
    private Customer customer;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private WalkInGuest walkInGuest;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roomTypeId", nullable = false)
    private RoomType roomType;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Partner partner;
    
    @OneToOne(optional = true)
    private RoomAllocationException roomAllocationException;

    public enum ReservationStatus {
        PENDING, CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT
    }

    public Reservation() {
        this.roomRates = new ArrayList<RoomRate>();
        this.rooms = new ArrayList<Room>();
    }

    public Reservation(Date checkInDate, Date checkOutDate, RoomType roomType, int numberOfRooms, ReservationStatus status, BigDecimal totalCost) {
        this();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.status = status;
        this.totalCost = totalCost;
    }

    // Getters and Setters

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the roomAllocationException
     */
    public RoomAllocationException getRoomAllocationException() {
        return roomAllocationException;
    }

    /**
     * @param roomAllocationException the roomAllocationException to set
     */
    public void setRoomAllocationException(RoomAllocationException roomAllocationException) {
        this.roomAllocationException = roomAllocationException;
    }

    /**
     * @return the numberOfRooms
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * @param numberOfRooms the numberOfRooms to set
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * @return the walkInGuest
     */
    public WalkInGuest getWalkInGuest() {
        return walkInGuest;
    }

    /**
     * @param walkInGuest the walkInGuest to set
     */
    public void setWalkInGuest(WalkInGuest walkInGuest) {
        this.walkInGuest = walkInGuest;
    }
}

