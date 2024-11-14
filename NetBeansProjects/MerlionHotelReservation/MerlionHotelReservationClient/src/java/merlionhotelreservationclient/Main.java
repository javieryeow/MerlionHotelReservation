import ejb.session.stateless.CreateCustomerSessionBeanRemote;
import ejb.session.stateless.CreateReservationSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List; 
import javax.ejb.EJB;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.exception.WrongPasswordException;

public class Main {

    @EJB
    private static CreateCustomerSessionBeanRemote createCustomerSessionBean;
    
    @EJB
    private static CreateReservationSessionBeanRemote createReservationSessionBean;
    
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws CustomerNotFoundException, WrongPasswordException {
        startClient();
    }

    public static void startClient() throws CustomerNotFoundException, WrongPasswordException {
        System.out.println("*** Welcome to Merlion Hotel Reservation System ***\n");
        System.out.println("1. Guest Login");
        System.out.println("2. Register as Guest");
        System.out.println("3. Exit");

        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:     
                System.out.print("Enter Guest Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
                try {
                    Customer loggedInCustomer = createCustomerSessionBean.customerLogin(email, password);
                    if (loggedInCustomer != null) {
                    System.out.println("Login successful! Welcome, " + loggedInCustomer.getFirstName() + ".");
                    performCustomerOperations(loggedInCustomer);
                    break;
                    }
                }
                catch (CustomerNotFoundException ex) {
                    System.out.println("Customer does not exist! Please register as guest first.");
                }
                catch (WrongPasswordException ex) {
                    System.out.println("Wrong password! Please try again.");
                }
            case 2:
                System.out.print("Enter First Name: ");
                String firstName = sc.nextLine();
                System.out.print("Enter Last Name: ");
                String lastName = sc.nextLine();
                System.out.print("Enter Email: ");
                String newEmail = sc.nextLine();
                System.out.print("Enter Phone Number: ");
                String phoneNumber = sc.nextLine();
                System.out.print("Enter Password: ");
                String newPassword = sc.nextLine();
                try {
                    Customer newCustomer = createCustomerSessionBean.registerAsCustomer(firstName, lastName, newEmail, phoneNumber, newPassword);
                    System.out.println("Registration successful! Welcome, " + newCustomer.getFirstName() + ".");
                    performCustomerOperations(newCustomer);
                }
                catch (CustomerAlreadyExistException ex) {
                    System.out.println("Customer Already Exists! Please log in instead.");
                }
        }
    }
        
    public static void performCustomerOperations(Customer customer) {
        System.out.println("1. Search Hotel Room");
        System.out.println("2. Reserve Hotel Room");
        System.out.println("3. View My Reservation Details");
        System.out.println("4. View All My Reservations");
        System.out.println("5. Logout");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                searchHotelRoom();
                break;
            case 2:
                reserveHotelRoom();
                break;
            case 3:
                viewReservationDetails();
                break;
            case 4:
                viewAllReservations();
                break;
            case 5:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    // 3. Search Hotel Room
    private static void searchHotelRoom() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            Date checkInDate = dateFormat.parse(sc.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            Date checkOutDate = dateFormat.parse(sc.nextLine());

            List<RoomType> availableRooms = createReservationSessionBean.searchAvailableRooms(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms for the selected dates.");
            } else {
                System.out.println("Available room types:");
                for (RoomType roomType : availableRooms) {
                    System.out.println("- " + roomType.getName() + " Room Type ID: " + roomType.getRoomTypeId());
                }
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
        }
    }

    // 4. Reserve Hotel Room
    private static void reserveHotelRoom() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            System.out.print("Enter your Customer ID: ");
            Long id = sc.nextLong();
            sc.nextLine();
            System.out.print("Please enter the room type ID to reserve: ");
            Long roomTypeId = sc.nextLong();
            sc.nextLine();
            System.out.print("Enter the number of rooms to reserve: ");
            int numberOfRooms = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            Date checkInDate = dateFormat.parse(sc.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            Date checkOutDate = dateFormat.parse(sc.nextLine());

            

            try {
                Reservation reservation = createReservationSessionBean.reserveHotelRoom(
                    id, roomTypeId, numberOfRooms, checkInDate, checkOutDate
                );
                System.out.println("Reservation successful. Reservation ID: " + reservation.getReservationId());

            } catch (RoomTypeNotFoundException | RoomTypeUnavailableException e) {
                System.out.println("Reservation failed: " + e.getMessage());
            }

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
        }
    }

    // 5. View My Reservation Details
    private static void viewReservationDetails() {
        
        System.out.print("Enter Customer ID: ");
        Long customerId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter reservation ID: ");
        Long reservationId = sc.nextLong();
        sc.nextLine();

        Reservation reservation = createReservationSessionBean.viewCustomerReservation(customerId, reservationId);
        if (reservation != null) {
            System.out.println("Reservation Details:");
            System.out.println("Check-in Date: " + reservation.getCheckInDate());
            System.out.println("Check-out Date: " + reservation.getCheckOutDate());
            System.out.println("Total Cost: " + reservation.getTotalCost());
            System.out.println("Status: " + reservation.getStatus());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    // 6. View All My Reservations
    private static void viewAllReservations() {
        
        System.out.print("Enter Customer ID: ");
        Long customerId = sc.nextLong();
        sc.nextLine();
        List<Reservation> reservations = createReservationSessionBean.viewAllReservations(customerId);
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.println("All Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId() + ", Check-in: " + reservation.getCheckInDate() + ", Check-out: " + reservation.getCheckOutDate() + ", Status: " + reservation.getStatus());
            }
        }
    }
}


        
        




                    
                    
                    
                    
              

    


