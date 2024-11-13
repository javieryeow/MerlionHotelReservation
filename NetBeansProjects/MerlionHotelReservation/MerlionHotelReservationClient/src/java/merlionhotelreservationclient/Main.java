import ejb.session.stateless.CreateCustomerSessionBeanRemote;
import ejb.session.stateless.CreateReservationSessionBeanRemote;
import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 
import javax.ejb.EJB;

public class Main {

    
    @EJB
    private CreateCustomerSessionBeanRemote createCustomerSessionBean;
    
    @EJB
    private CreateReservationSessionBeanRemote createReservationSessionBean;
    
    private boolean isGuestLoggedIn = false;
    private Customer loggedInCustomer;

    public Main() {
       
    }

   public static void main(String[] args) {
        Main clientApp = new Main();
        clientApp.run();
    }

    public void run() {
    Scanner scanner = new Scanner(System.in);
    boolean isLoggedIn = false;

    while (true) {
        if (!isLoggedIn) {
            // Initial menu - Login or Register only
            System.out.println("*** Welcome to Guest Login System ***\n");
            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    isLoggedIn = guestLogin(); // Update isLoggedIn based on guestLogin result
                    if (isLoggedIn) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed. Please try again.");
                    }
                    break;
                case 2:
                    isLoggedIn = registerAsGuest(); // Assume registerAsGuest() also returns true if successful
                    if (isLoggedIn) {
                        System.out.println("Registration successful!");
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } else {
            // Main menu after login or registration
            System.out.println("3. Search Hotel Room");
            System.out.println("4. Reserve Hotel Room");
            System.out.println("5. View My Reservation Details");
            System.out.println("6. View All My Reservations");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 3:
                    searchHotelRoom();
                    break;
                case 4:
                    reserveHotelRoom();
                    break;
                case 5:
                    viewReservationDetails();
                    break;
                case 6:
                    viewAllReservations();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    return; // Exit the program
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}


    // 1. Guest Login
public boolean guestLogin() {
    if (isGuestLoggedIn) {
        System.out.println("You are already logged in.");
        return true;
    }

    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter email: ");
    String email = scanner.nextLine();

    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    try {
        Customer customer = createCustomerSessionBean.customerLogin(email, password);
        isGuestLoggedIn = true;
        loggedInCustomer = customer;
        System.out.println("Welcome, " + customer.getFirstName());
        return true;
    } catch (CustomerNotFoundException e) {
        System.out.println("Login failed: " + e.getMessage());
    } catch (WrongPasswordException e) {
        System.out.println("Login failed: " + e.getMessage());
    } 
    
    return false;
}


    // 2. Register as Guest
public boolean registerAsGuest() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter first name: ");
    String firstName = scanner.nextLine();

    System.out.print("Enter last name: ");
    String lastName = scanner.nextLine();

    System.out.print("Enter email: ");
    String email = scanner.nextLine();

    System.out.print("Enter password: ");
    String password = scanner.nextLine();

    System.out.print("Enter phone number: ");
    String phoneNumber = scanner.nextLine();

    // Call the EJB method for registration
    Customer customer = createCustomerSessionBean.registerAsCustomer(firstName, lastName, email, password, phoneNumber);
    if (customer != null) {
        System.out.println("Registration successful. Welcome, " + customer.getFirstName());
        isGuestLoggedIn = true; // Set logged-in status for the user
        loggedInCustomer = customer; // Store the logged-in customer information
        return true; // Indicate registration was successful
    } else {
        System.out.println(" A user with that email may already exist.");
        return false; // Indicate registration failure
    }
}


    // 3. Search Hotel Room
    public void searchHotelRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());

        List<RoomType> availableRooms = createReservationSessionBean.searchAvailableRooms(checkInDate, checkOutDate);
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for the selected dates.");
        } else {
            System.out.println("Available room types:");
            for (RoomType roomType : availableRooms) {
                System.out.println("- " + roomType.getName());
            }
        }
    }

    // 4. Reserve Hotel Room
   public void reserveHotelRoom() {
    if (!isGuestLoggedIn) {
        System.out.println("Please log in first.");
        return;
    }

    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter check-in date (YYYY-MM-DD): ");
    LocalDate checkInDate = LocalDate.parse(scanner.nextLine());

    System.out.print("Enter check-out date (YYYY-MM-DD): ");
    LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());

    System.out.print("Enter room type ID(s) to reserve: ");
    String[] roomTypeIdsStr = scanner.nextLine().split(",");
    List<Long> roomTypeIds = new ArrayList<>();
    for (String idStr : roomTypeIdsStr) {
        roomTypeIds.add(Long.parseLong(idStr.trim()));
    }

    try {
        // Attempt to reserve the hotel room(s) with provided details
        Reservation reservation = createReservationSessionBean.reserveHotelRoom(
            loggedInCustomer, roomTypeIds, checkInDate, checkOutDate
        );
        System.out.println("Reservation successful. Reservation ID: " + reservation.getReservationId());
    
    } catch (RoomTypeNotFoundException e) {
        // Handle case where a room type ID does not correspond to any existing room type
        System.out.println("Reservation failed: " + e.getMessage());
    
    } catch (RoomTypeUnavailableException e) {
        // Handle case where a room type is not available for the specified dates
        System.out.println("Reservation failed: " + e.getMessage());
    
    } 
}


    // 5. View My Reservation Details
    public void viewReservationDetails() {
        if (!isGuestLoggedIn) {
            System.out.println("Please log in first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter reservation ID: ");
        Long reservationId = scanner.nextLong();

        Reservation reservation = createReservationSessionBean.viewCustomerReservation(loggedInCustomer.getCustomerId(), reservationId);
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
    public void viewAllReservations() {
        if (!isGuestLoggedIn) {
            System.out.println("Please log in first.");
            return;
        }

        List<Reservation> reservations = createReservationSessionBean.viewAllReservations();
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

