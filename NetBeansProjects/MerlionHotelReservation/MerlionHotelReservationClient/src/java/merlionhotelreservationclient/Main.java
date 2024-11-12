import ejb.session.stateless.CreateCustomerSessionBeanRemote;
import ejb.session.stateless.CreateReservationSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private CreateCustomerSessionBeanRemote createCustomerSessionBean;
    private CreateReservationSessionBeanRemote createReservationSessionBean;
    private boolean isGuestLoggedIn = false;
    private Customer loggedInCustomer;

    public Main(CreateCustomerSessionBeanRemote createCustomerSessionBean, CreateReservationSessionBeanRemote createReservationSessionBean) {
        this.createCustomerSessionBean = createCustomerSessionBean;
        this.createReservationSessionBean = createReservationSessionBean;
    }

    public static void main(String[] args) {
        // Assuming beans are injected or retrieved via JNDI lookup
        Main clientApp = new Main(null, null); // replace nulls with actual bean lookups
        clientApp.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Search Hotel Room");
            System.out.println("4. Reserve Hotel Room");
            System.out.println("5. View My Reservation Details");
            System.out.println("6. View All My Reservations");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    guestLogin();
                    break;
                case 2:
                    registerAsGuest();
                    break;
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

    // 1. Guest Login
    public void guestLogin() {
        if (isGuestLoggedIn) {
            System.out.println("You are already logged in.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = createCustomerSessionBean.customerLogin(email, password);
        if (customer != null) {
            isGuestLoggedIn = true;
            loggedInCustomer = customer;
            System.out.println("Login successful. Welcome, " + customer.getFirstName());
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    // 2. Register as Guest
    public void registerAsGuest() {
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

        Customer customer = createCustomerSessionBean.createCustomer(firstName, lastName, email, password, phoneNumber);
        if (customer != null) {
            System.out.println("Registration successful. You may now log in.");
        } else {
            System.out.println("Registration failed. Please try again.");
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
                System.out.println("- " + roomType.getName() + ": " + roomType.getBasePrice() + " per night");
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

        System.out.print("Enter room type ID(s) to reserve (comma-separated): ");
        String[] roomTypeIdsStr = scanner.nextLine().split(",");
        List<Long> roomTypeIds = new ArrayList<>();
        for (String idStr : roomTypeIdsStr) {
            roomTypeIds.add(Long.parseLong(idStr.trim()));
        }

        try {
            CustomerReservation reservation = createReservationSessionBean.reserveHotelRoom(loggedInCustomer, roomTypeIds, checkInDate, checkOutDate);
            System.out.println("Reservation successful. Reservation ID: " + reservation.getReservationId());
        } catch (Exception e) {
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

        CustomerReservation reservation = createReservationSessionBean.viewCustomerReservation(loggedInCustomer.getCustomerId(), reservationId);
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

        List<CustomerReservation> reservations = createReservationSessionBean.viewAllReservationsForCustomer(loggedInCustomer.getCustomerId());
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            System.out.println("All Reservations:");
            for (CustomerReservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId() + ", Check-in: " + reservation.getCheckInDate() + ", Check-out: " + reservation.getCheckOutDate() + ", Status: " + reservation.getStatus());
            }
        }
    }
}

