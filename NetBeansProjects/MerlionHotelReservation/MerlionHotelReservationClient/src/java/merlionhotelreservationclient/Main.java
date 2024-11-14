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
import util.exception.CustomerNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.exception.WrongPasswordException;

public class Main {

    @EJB
    private CreateCustomerSessionBeanRemote createCustomerSessionBean;
    
    @EJB
    private CreateReservationSessionBeanRemote createReservationSessionBean;
    
    private boolean isGuestLoggedIn = false;
    private Customer loggedInCustomer;

    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;

        while (true) {
            if (!isLoggedIn) {
                System.out.println("*** Welcome to Guest Login System ***\n");
                System.out.println("1. Guest Login");
                System.out.println("2. Register as Guest");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        isLoggedIn = guestLogin();
                        if (isLoggedIn) {
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Login failed. Please try again.");
                        }
                        break;
                    case 2:
                        isLoggedIn = registerAsGuest();
                        if (isLoggedIn) {
                            System.out.println("Registration successful!");
                        } else {
                            System.out.println("Registration failed. Please try again.");
                        }
                        break;
                    case 3:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("3. Search Hotel Room");
                System.out.println("4. Reserve Hotel Room");
                System.out.println("5. View My Reservation Details");
                System.out.println("6. View All My Reservations");
                System.out.println("7. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

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
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    // 1. Guest Login
    public static boolean guestLogin() {
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
        } catch (CustomerNotFoundException | WrongPasswordException e) {
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

        Customer customer = createCustomerSessionBean.registerAsCustomer(firstName, lastName, email, password, phoneNumber);
        if (customer != null) {
            System.out.println("Registration successful. Welcome, " + customer.getFirstName());
            isGuestLoggedIn = true;
            loggedInCustomer = customer;
            return true;
        } else {
            System.out.println("A user with that email may already exist.");
            return false;
        }
    }

    // 3. Search Hotel Room
    public void searchHotelRoom() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            Date checkInDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            Date checkOutDate = dateFormat.parse(scanner.nextLine());

            List<RoomType> availableRooms = createReservationSessionBean.searchAvailableRooms(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms for the selected dates.");
            } else {
                System.out.println("Available room types:");
                for (RoomType roomType : availableRooms) {
                    System.out.println("- " + roomType.getName());
                }
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
        }
    }

    // 4. Reserve Hotel Room
    public void reserveHotelRoom() {
        if (!isGuestLoggedIn) {
            System.out.println("Please log in first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            Date checkInDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            Date checkOutDate = dateFormat.parse(scanner.nextLine());

            System.out.print("Please enter the room type ID to reserve, or enter multiple IDs to reserve multiple rooms: ");
            String[] roomTypeIdsStr = scanner.nextLine().split(",");
            List<Long> roomTypeIds = new ArrayList<>();
            for (String idStr : roomTypeIdsStr) {
                roomTypeIds.add(Long.parseLong(idStr.trim()));
            }

            try {
                Reservation reservation = createReservationSessionBean.reserveHotelRoom(
                    loggedInCustomer, roomTypeIds, checkInDate, checkOutDate
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

        List<Reservation> reservations = createReservationSessionBean.viewAllReservations(loggedInCustomer.getCustomerId());
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


