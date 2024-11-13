/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.*;
import entity.Room.RoomStatus;
import entity.RoomAllocationException.RoomAllocationExceptionType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.Query;

/**
 *
 * @author javieryeow
 */
@Stateless
public class AllocatingRoomSessionBean implements AllocatingRoomSessionBeanRemote, AllocatingRoomSessionBeanLocal {

    @EJB
    private CreateRoomAllocationExceptionSessionBeanLocal createRoomAllocationExceptionSessionBean;
    
    
    
    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public void allocateRoomForReservation(Date checkInDate) {
        Query query = em.createQuery("SELECT res FROM Reservation res WHERE res.checkinDate = :inCheckinDate");
        query.setParameter("inCheckinDate", checkInDate);
        List<Reservation> reservations = (List<Reservation>) query.getResultList();
        for (Reservation r: reservations) {
            if(!hasAllocated(r)) {
                int numberOfRooms = r.getNumberOfRooms();
                RoomType roomType = r.getRoomType();
                RoomType nextHigherRoomType = roomType.getHigherRoomType();
                Query queryRooms = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :inRoomType AND r.roomStatus = :inRoomStatus AND r.enabled = :inEnabled");
                queryRooms.setParameter("inRoomType", roomType);
                queryRooms.setParameter("inRoomStatus", RoomStatus.AVAILABLE);
                queryRooms.setParameter("inEnabled", true);
                List<Room> results = (List<Room>) queryRooms.getResultList();
                List<Room> requestedRoom = new ArrayList<Room>();
                List<Room> confirmedRooms = new ArrayList<Room>();
                for (Room room: results) {
                    if (isNotOccupied(room)) {
                        requestedRoom.add(room);
                    }
                }
                if(requestedRoom.size() >= numberOfRooms) {
                    for(Room room: requestedRoom)
                    {
                        confirmedRooms.add(room);
                        if(confirmedRooms.size() == numberOfRooms) 
                        {
                            break;
                        }
                    }
                } else if (requestedRoom.size() < numberOfRooms && nextHigherRoomType != null) {
                    Query queryHigherRoomType = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :inRoomType AND r.roomStatus = :inRoomStatus AND r.enabled = :inEnabled");
                    queryHigherRoomType.setParameter("inRoomType", nextHigherRoomType);
                    queryHigherRoomType.setParameter("inStatus", RoomStatus.AVAILABLE);
                    queryHigherRoomType.setParameter("inEnabled", true);
                    List<Room> resultsHigher = (List<Room>) queryHigherRoomType.getResultList();
                    List<Room> roomsHigherQuery = new ArrayList<>();
                    for (Room room: resultsHigher) {
                        if (isNotOccupied(room)) {
                            roomsHigherQuery.add(room);
                        }
                    }
                    if((requestedRoom.size() + roomsHigherQuery.size()) >= numberOfRooms) {
                        for (Room room: requestedRoom) {
                            confirmedRooms.add(room);
                        }
                        for (Room higherRoom: roomsHigherQuery) {
                            confirmedRooms.add(higherRoom);
                            if(confirmedRooms.size() == numberOfRooms) {
                                break;
                            }
                        }
                        RoomAllocationExceptionType type = RoomAllocationExceptionType.UPGRADE;
                        String issue = "Insufficient rooms of requested type, room of higher type was assigned!";
                        Long roomAllocationExceptionId = createRoomAllocationExceptionSessionBean.createRoomAllocationException(type, issue, r);
                    } else {
                        RoomAllocationExceptionType type = RoomAllocationExceptionType.NO_UPGRADE;
                        String issue = "Insufficient rooms of requested type, insufficient rooms of higher type available for assignment!";
                        Long roomAllocationExceptionId = createRoomAllocationExceptionSessionBean.createRoomAllocationException(type, issue, r);
                    }
                } else {
                    RoomAllocationExceptionType type = RoomAllocationExceptionType.NO_UPGRADE;
                    String issue = "Insufficient rooms of requested type, no higher room type to be upgraded to!";
                    Long roomAllocationExceptionId = createRoomAllocationExceptionSessionBean.createRoomAllocationException(type, issue, r);
                }
                for (Room room: confirmedRooms) {
                    r.getRooms().add(room);
                    room.getReservations().add(r);
                }
            }     
        }
    }
    
    private boolean hasAllocated(Reservation reservation) {
        List<Room> rooms = reservation.getRooms();
        if (rooms.size() != 0) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isNotOccupied(Room room){
        List<Reservation> reservations = room.getReservations();
        boolean free = true;
        for(Reservation r : reservations){
            if(r.getCheckOutDate().after(new Date())){
                free = false;
                break;
            }
        }
        return free;
    }
}
    
