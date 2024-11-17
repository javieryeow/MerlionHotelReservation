/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystemseclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import ws.merlionhotelreservation.InvalidPartnerLoginException_Exception;
import ws.merlionhotelreservation.Partner;
import ws.merlionhotelreservation.PartnerNotFoundException;
import ws.merlionhotelreservation.PartnerWebService_Service;
import ws.merlionhotelreservation.Reservation;
import ws.merlionhotelreservation.ReservationNotFoundException_Exception;
import ws.merlionhotelreservation.RoomType;
import ws.merlionhotelreservation.RoomTypeNotFoundException;
import ws.merlionhotelreservation.RoomTypeUnavailableException;
import ws.merlionhotelreservation.WrongPasswordException_Exception;

/**
 *  
 * @author javieryeow
 */
public class HolidayReservationSystemSeClient {
    
    private static Scanner sc = new Scanner(System.in);
    
    static PartnerWebService_Service service = new PartnerWebService_Service();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws WrongPasswordException_Exception, InvalidPartnerLoginException_Exception, ParseException, ReservationNotFoundException_Exception {
        startClient();
    }
    
    public static void startClient() throws WrongPasswordException_Exception, InvalidPartnerLoginException_Exception, ParseException, ReservationNotFoundException_Exception {
        System.out.println("***** Welcome to the Holiday Reservation System! *****\n");
        System.out.println("-------------------------------------------------");
        System.out.println("1. Partner Login");
        System.out.println("2. Exit");
        
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1: 
                System.out.print("Enter Partner Username: ");
                String username = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
                Partner loggedInPartner = service.getPartnerWebServicePort().partnerLogin(username, password);
                if (loggedInPartner != null) {
                    System.out.println("Login successful! Welcome, " + loggedInPartner.getUsername() + ".");
                    performPartnerOperations(loggedInPartner);
                }
                break;
            case 2: 
                System.out.println("Exiting Holiday Reservation System. Goodbye!");
                return;
        }
    }
    
    public static void performPartnerOperations(Partner loggedInPartner) throws ParseException, ReservationNotFoundException_Exception {
        while (true) {
            System.out.println("1. Search Hotel Room");
            System.out.println("2. Reserve Hotel Room");
            System.out.println("3. View Partner Reservation Details");
            System.out.println("4. View All Partner Reservations");
            System.out.println("5. Logout");
            
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    partnerSearchAvailableRoom();
                    break;
                case 2:
                    reservePartnerHotelRoom();
                    break;
                case 3:
                    viewPartnerReservationDetails();
                    break;
                case 4:
                    viewAllPartnerReservations();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void partnerSearchAvailableRoom() throws ParseException {
//        System.out.print("Enter check-in date (DD-MM-YYYY): ");
//        String checkInDate = sc.nextLine();
//        System.out.print("Enter check-out date (DD-MM-YYYY): ");
//        String checkOutDate = sc.nextLine();
//        List<RoomType> availableRooms = service.getPartnerWebServicePort().partnerSearchAvailableRooms(checkInDate, checkOutDate);
//        if (availableRooms.isEmpty()) {
//            System.out.println("No available rooms for the selected dates.");
//        } else {
//            System.out.printf("%-15s %-20s\n", "Room Type ID", "Name");
//            System.out.println("----------------------------------------------");
//            for (RoomType roomType : availableRooms) {
//                if (roomType.getEnabled()) {
//                    System.out.printf("%-15d %-20s\n", roomType.getRoomTypeId(), roomType.getName());
//                }
//            }
//        }
    }
    
    private static void reservePartnerHotelRoom() {
//        System.out.print("Enter your Partner ID: ");
//        Long partnerId = sc.nextLong();
//        sc.nextLine();
//        System.out.print("Enter the Customer ID for the reservation: ");
//        Long customerId = sc.nextLong();
//        sc.nextLine();
//        System.out.print("Please enter the room type ID to reserve: ");
//        Long roomTypeId = sc.nextLong();
//        sc.nextLine();
//        System.out.print("Enter the number of rooms to reserve: ");
//        int numberOfRooms = sc.nextInt();
//        sc.nextLine();
//        System.out.print("Enter check-in date (DD-MM-YYYY): ");
//        String checkInDate = sc.nextLine();
//        System.out.print("Enter check-out date (DD-MM-YYYY): ");
//        String checkOutDate = sc.nextLine();
//        Reservation reservation = service.getPartnerWebServicePort().partnerReserveRoom(partnerId, customerId, roomTypeId, numberOfRooms, checkInDate, checkOutDate);
//        System.out.println("Reservation successful. Reservation ID: " + reservation.getReservationId());
    }
    
    private static void viewPartnerReservationDetails() throws ReservationNotFoundException_Exception {
        System.out.print("Enter Partner ID: ");
        Long partnerId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter Reservation ID: ");
        Long reservationId = sc.nextLong();
        sc.nextLine();
        Reservation reservation = service.getPartnerWebServicePort().viewPartnerReservation(partnerId, reservationId);
        if (reservation != null) {
            System.out.println("Reservation Details:");
            System.out.printf("%-20s: %s\n", "Check-in Date", reservation.getCheckInDate());
            System.out.printf("%-20s: %s\n", "Check-out Date", reservation.getCheckOutDate());
            System.out.printf("%-20s: %s\n", "Total Cost", reservation.getTotalCost());
            System.out.printf("%-20s: %s\n", "Status", reservation.getStatus());
            System.out.printf("%-20s: %d\n", "Number of Rooms", reservation.getNumberOfRooms());
            System.out.printf("%-20s: %s\n", "Room Type", reservation.getRoomType().getName());
        } else {
            System.out.println("Reservation not found.");
        }
    }
    
    private static void viewAllPartnerReservations() {
        System.out.print("Enter Partner ID: ");
        Long partnerId = sc.nextLong();
        sc.nextLine();
        List<Reservation> reservations = service.getPartnerWebServicePort().viewAllPartnerReservations(partnerId);
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.printf("%-15s %-15s %-15s %-15s\n", "Reservation ID", "Check-in Date", "Check-out Date", "Status");
            System.out.println("-------------------------------------------------------------------");

            for (Reservation reservation : reservations) {
                System.out.printf("%-15d %-15s %-15s %-15s\n",
                        reservation.getReservationId(),
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate(),
                        reservation.getStatus());
            }
        }
    }
    
    
    
}
