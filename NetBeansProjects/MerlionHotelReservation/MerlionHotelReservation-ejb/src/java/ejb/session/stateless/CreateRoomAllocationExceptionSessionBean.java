/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomAllocationException;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author javieryeow
 */
@Stateless
public class CreateRoomAllocationExceptionSessionBean implements CreateRoomAllocationExceptionSessionBeanRemote, CreateRoomAllocationExceptionSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    public Long createRoomAllocationException() {
        List<RoomAllocationException> exceptions = new ArrayList<>();

        List<Reservation> reservations = getReservationsForDate(reportDate);

        for (Reservation reservation : reservations) {
            RoomType reservedRoomType = reservation.getRoomType();
            RoomType nextHigherRoomType = findNextHigherRoomType(reservedRoomType);

            if (!isRoomAvailable(reservedRoomType, reportDate)) {
                if (nextHigherRoomType != null && isRoomAvailable(nextHigherRoomType, reportDate)) {
                    // Exception Type 1: Auto-upgrade to next room type
                    allocateRoomToReservation(reservation, nextHigherRoomType);
                    exceptions.add(new RoomAllocationException(reservation, "Auto-upgraded to higher room type"));
                } else {
                    // Exception Type 2: No rooms available in reserved or higher type
                    exceptions.add(new RoomAllocationException(reservation, "No room available"));
                }
            }
        }

        return exceptions;
    }

    private List<Reservation> getReservationsForDate(Date date) {
        // Query to fetch reservations for the specified date
    }

    private boolean isRoomAvailable(RoomType roomType, Date date) {
        // Logic to check room availability in specified room type
    }

    private RoomType findNextHigherRoomType(RoomType roomType) {
        // Logic to find the next higher room type
    }

    private void allocateRoomToReservation(Reservation reservation, RoomType roomType) {
        // Logic to allocate a room from the specified room type to the reservation
    }
}

    public void persist(Object object) {
        em.persist(object);
    }

   
}
