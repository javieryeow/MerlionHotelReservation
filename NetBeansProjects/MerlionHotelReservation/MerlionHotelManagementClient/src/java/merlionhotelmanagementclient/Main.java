/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package merlionhotelmanagementclient;

import ejb.session.stateless.AllocatingRoomSessionBeanRemote;
import ejb.session.stateless.CreateCustomerSessionBeanRemote;
import ejb.session.stateless.CreateEmployeeSessionBeanRemote;
import ejb.session.stateless.CreateReservationSessionBeanRemote;
import ejb.session.stateless.CreateRoomAllocationExceptionSessionBeanRemote;
import ejb.session.stateless.EmployeeLoginRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Employee.role;
import java.util.Scanner;
import javax.ejb.EJB;
import java.util.*;
import entity.*;
import entity.Room.RoomStatus;
import entity.RoomRate.RateType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import util.exception.EmployeeAlreadyExistsException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.RoomTypeUnavailableException;
import util.exception.InvalidEmployeeLoginException;
import ejb.session.stateless.PartnerLoginSessionBeanRemote;

/**
 *
 * @author javieryeow
 */
public class Main {

    @EJB
    private static CreateRoomAllocationExceptionSessionBeanRemote createRoomAllocationExceptionSessionBean;

    @EJB
    private static AllocatingRoomSessionBeanRemote allocatingRoomSessionBean;

    @EJB
    private static CreateReservationSessionBeanRemote createReservationSessionBean;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBean;

    @EJB
    private static EmployeeLoginRemote employeeLogin;

    @EJB
    private static PartnerLoginSessionBeanRemote createPartnerSessionBean;

    @EJB
    private static CreateEmployeeSessionBeanRemote createEmployeeSessionBean;

    @EJB
    private static CreateCustomerSessionBeanRemote createCustomerSessionBean;
    
    
    
    private static Scanner sc = new Scanner(System.in);
     
    public static void main(String[] args) throws RoomTypeNotFoundException, ReservationNotFoundException, InvalidEmployeeLoginException, Exception {
        startManagementClient();
    }
    
    public static void startManagementClient() throws RoomTypeNotFoundException, ReservationNotFoundException, InvalidEmployeeLoginException, Exception {
        System.out.println("*** Welcome to Employee Login System ***\n");
        System.out.print("Enter Employee Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();
        
        try {
            Employee employee = employeeLogin.login(username, password);
            if (employee != null) {
                System.out.println("Login successful! Welcome, Employee " + employee.getEmployeeId() + ".");
                if (employee.getStatus().equals(role.OPERATION_MANAGER)) {
                    operationManagerMenu(employee);
                } else if (employee.getStatus().equals(role.GUEST_RELATIONS_OFFICER)) {
                    guestRelationOfficerMenu(employee);
                } else if (employee.getStatus().equals(role.SALES_MANAGER)) {
                    salesManagerMenu(employee);
                } else {
                    systemAdminMenu(employee);
                }
            }
        } catch (InvalidEmployeeLoginException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
        }
    }
    
    public static void operationManagerMenu(Employee employee) throws RoomTypeNotFoundException {
        while (true) {
            System.out.println("1. Create New Room Type");
            System.out.println("2. Update Room Type");
            System.out.println("3. Delete Room Type");
            System.out.println("4. View All Room Types");
            System.out.println("5. Create New Room");
            System.out.println("6. Update Room");
            System.out.println("7. Delete Room");
            System.out.println("8. View All Rooms");
            System.out.println("9. View Room Allocation Exception Report");
            System.out.println("10. View Room Type Details");
            System.out.println("11. Allocate Rooms to Reservations");
            System.out.println("12. Logout");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    createNewRoomType();
                    break;
                case 2:
                    updateRoomType();
                    break;
                case 3:
                    deleteRoomType();
                    break;
                case 4:
                    viewAllRoomTypes();
                    break;
                case 5:
                    createNewRoom();
                    break;
                case 6:
                    updateRoom();
                    break;
                case 7:
                    deleteRoom();
                    break;
                case 8:
                    viewAllRooms();
                    break;
                case 9:
                    viewRoomAllocationExceptionReport();
                    break;
                case 10:
                    viewRoomTypeDetails();
                    break;
                case 11:
                    allocateRoomsToReservations();
                    break;
                case 12:
                    System.out.println("Logging out..."); 
                    return;
                default: System.out.println("Invalid choice");
            }
        }  
    }
    
    public static void guestRelationOfficerMenu(Employee employee) throws ReservationNotFoundException, Exception {
        while (true) {
            System.out.println("1. Walk-in Search Room");
            System.out.println("2. Walk-in Reserve Room");
            System.out.println("3. Check-in Guest");
            System.out.println("4. Check-out Guest");
            System.out.println("5. Logout");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    walkInSearchRoom();
                    break;
                case 2:
                    walkInReserveRoom();
                    break;
                case 3:
                    checkInGuest();
                    break;
                case 4:
                    checkOutGuest();
                    break;
                case 5:
                    System.out.println("Logging out..."); 
                    return;
                default: System.out.println("Invalid choice");
            }
        }
    }
    
    public static void salesManagerMenu(Employee employee) throws RoomTypeNotFoundException, RoomRateNotFoundException {
        while (true) {
            System.out.println("1. Create new Room Rate");
            System.out.println("2. View Room Rate Details");
            System.out.println("3. Update Room Rate");
            System.out.println("4. Delete Room Rate");
            System.out.println("5. View All Room Rates");
            System.out.println("6. Logout");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    createNewRoomRate();
                    break;
                case 2:
                    viewRoomRateDetails();
                    break;
                case 3:
                    updateRoomRate();
                    break;
                case 4:
                    deleteRoomRate();
                    break;
                case 5:
                    viewAllRoomRates();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
            }
        } 
    }
    
    public static void systemAdminMenu(Employee employee) {
        while (true) {
            System.out.println("1. Create New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Create New Partner");
            System.out.println("4. View All Partners");
            System.out.println("5. Logout");

            int choice = sc.nextInt();
            sc.nextLine();
            switch(choice) {
                case 1:
                    createNewEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    createNewPartner();
                    break;
                case 4:
                    viewAllPartners();
                    break;
                case 5:
                    System.out.println("Logging out....");
                    return;
            }
        }    
    }
    
    // OPERATION MANAGER METHODS
    
    private static void createNewRoomType() throws RoomTypeNotFoundException {
        System.out.print("Enter Room Type Name: ");
        String name = sc.nextLine();
        System.out.print("Enter description: ");
        String description = sc.nextLine();
        System.out.print("Enter size: ");
        int size = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter number of beds: ");
        int bed = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter amenities: ");
        String amenities = sc.nextLine();
        System.out.print("Enter name of nextHigherRoomType: ");
        String nextHigherRoomType = sc.nextLine();
        try {
            Long roomTypeId = roomTypeSessionBean.createRoomType(name, description, size, bed, capacity, amenities, nextHigherRoomType);
            System.out.println("Room Type successfully created! Room Type ID: " + roomTypeId);
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void updateRoomType() {
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter New Room Type Name: ");
        String name = sc.nextLine();
        System.out.print("Enter new description: ");
        String description = sc.nextLine();
        System.out.print("Enter new size: ");
        int size = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new number of beds: ");
        int bed = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new amenities: ");
        String amenities = sc.nextLine();
        roomTypeSessionBean.updateRoomType(roomTypeId, name, description, size, bed, capacity, amenities);
        System.out.println("Room Type Successfully Updated!");
    }
    
    private static void deleteRoomType() {
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        roomTypeSessionBean.deleteRoomType(roomTypeId);
        System.out.println("Room Type Successfully Deleted!");
    }
    
    private static void viewAllRoomTypes() {
        List<RoomType> list = roomTypeSessionBean.viewAllRoomTypes();
        System.out.printf("%-10s %-20s %-50s %-10s %-10s %-10s %-50s\n", 
                      "RoomType ID", "Name", "Description", "Size", "Beds", "Capacity", "Amenities");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        // Print each RoomType in a formatted row
        for (RoomType roomtype : list) {
            System.out.printf("%-10d %-20s %-50s %-10s %-10s %-10d %-50s\n",
                        roomtype.getRoomTypeId(),
                        roomtype.getName(),
                        roomtype.getDescription(),
                        roomtype.getSize(),
                        roomtype.getBed(),
                        roomtype.getCapacity(),
                        roomtype.getAmenities());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void viewRoomTypeDetails() {
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        try {
            RoomType roomtype = roomTypeSessionBean.findRoomTypeById(roomTypeId);
            System.out.printf("%-20s: %d\n", "Room Type ID", roomtype.getRoomTypeId());
            System.out.printf("%-20s: %s\n", "Name", roomtype.getName());
            System.out.printf("%-20s: %s\n", "Description", roomtype.getDescription());
            System.out.printf("%-20s: %s\n", "Size", roomtype.getSize());
            System.out.printf("%-20s: %s\n", "Bed Type", roomtype.getBed());
            System.out.printf("%-20s: %d\n", "Capacity", roomtype.getCapacity());
            System.out.printf("%-20s: %s\n", "Amenities", roomtype.getAmenities());
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void createNewRoom() throws RoomTypeNotFoundException {
        System.out.print("Enter Room Type Name: ");
        String roomTypeName = sc.nextLine();
        System.out.println("Enter Room number: ");
        String roomNumber = sc.nextLine();
        try {
            Long roomId = roomSessionBean.createRoom(roomNumber, roomTypeName);
            System.out.println("Room created successfully! Room ID: " + roomId);
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void updateRoom() throws RoomTypeNotFoundException {
        System.out.print("Enter Room ID: ");
        Long roomId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter New Room Type Name: ");
        String roomTypeName = sc.nextLine();
        System.out.println("Enter New Room number: ");
        String roomNumber = sc.nextLine();
        System.out.print("Enter Room Availability(1: AVAILABLE, 2: UNAVAILABLE): ");
        int choice = sc.nextInt();
        sc.nextLine();
        RoomStatus status = RoomStatus.AVAILABLE;
        if (choice == 1) {
            status = RoomStatus.AVAILABLE;
        } else {
            status = RoomStatus.NOT_AVAILABLE;
        }
        try {
            RoomType roomType = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
            roomSessionBean.updateRoom(roomId, roomNumber, roomType, status);
            System.out.println("Room Successfully Updated!");
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private static void deleteRoom() {
        System.out.print("Enter Room ID: ");
        Long roomId = sc.nextLong();
        roomSessionBean.deleteRoom(roomId);
        System.out.println("Room Successfully Deleted!");
    }
    
    private static void viewAllRooms() {
        List<Room> list = roomSessionBean.viewAllRooms();
        System.out.printf("Room ID", "Room Number", "Room Type", "Room Availability");
        for (Room room: list) {
            System.out.printf("%10s%20s%30s%30s\n",room.getRoomId(),room.getRoomNumber(), room.getRoomType().getName(), room.getStatus());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void viewRoomAllocationExceptionReport() {
        List<RoomAllocationException> list = createRoomAllocationExceptionSessionBean.viewAllRoomAllocationException();
        System.out.printf("%-25s %-15s %-50s %-20s\n", "Exception ID", "Type", "Issue", "Reservation ID");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (RoomAllocationException exception : list) {
            Long reservationId = (exception.getReservation() != null) ? exception.getReservation().getReservationId() : null;

            System.out.printf("%-25s %-15s %-50s %-20s\n", 
                              exception.getRoomAllocationExceptionId(), 
                              exception.getType(), 
                              exception.getIssue(), 
                              reservationId != null ? reservationId : "N/A");
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void allocateRoomsToReservations() {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yy");
            System.out.print("Enter Check In Date to allocate Reservations (dd-mm-yyyy): ");
            Date date = inputDateFormat.parse(sc.nextLine().trim());
            allocatingRoomSessionBean.allocateRoomForReservation(date);
            System.out.print("All rooms for reservations on " + date + " have been allocated!");
        } catch (ParseException ex) {
            System.out.println("Invalid date format. Please enter the date in dd-mm-yyyy format.");
        }
    }
    
    // SALES MANAGER METHODS
    
    private static void createNewRoomRate() throws RoomTypeNotFoundException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yy");
        System.out.print("Enter Room Rate Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Room Type Name: ");
        String roomTypeName = sc.nextLine();
        System.out.println("Select Rate Type (1: PUBLISHED, 2: NORMAL, 3: PEAK, 4: PROMOTION): ");
        int choice = sc.nextInt();
        RateType type = RateType.PUBLISHED;

        if (choice == 1) {
            type = RateType.PUBLISHED;
        } else if (choice == 2) {
            type = RateType.NORMAL;
        } else if (choice == 3) {
            type = RateType.PEAK;
        } else if (choice == 4) {
            type = RateType.PROMOTION;
        }

        System.out.print("Enter Rate Per Night: ");
        BigDecimal ratePerNight = sc.nextBigDecimal();
        sc.nextLine();

        Date start = null;
        Date end = null;

        if (choice == 3 || choice == 4) {
            try {
                System.out.print("Enter Start Date (dd-mm-yyyy): ");
                start = inputDateFormat.parse(sc.nextLine().trim());
                System.out.print("Enter End Date (dd-mm-yyyy): ");
                end = inputDateFormat.parse(sc.nextLine().trim());
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Please enter the date in dd-mm-yyyy format.");
                return; // Exit method if parsing fails
            }
        }
        try {
            Long roomRateId = roomRateSessionBean.createRoomRate(name, roomTypeName, type, ratePerNight, start, end);
            System.out.println("Room Rate Successfully Created! Room Rate ID: " + roomRateId);
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    private static void viewRoomRateDetails() throws RoomRateNotFoundException {
        try {
            System.out.print("Enter Room Rate ID: ");
            Long roomRateId = sc.nextLong();
            sc.nextLine();  // Consume newline
            RoomRate roomRate = roomRateSessionBean.findRoomRateById(roomRateId);

            // Display headers
            System.out.printf("%-15s: %d\n", "Room Rate ID", roomRate.getRoomRateId());
            System.out.printf("%-15s: %s\n", "Room Rate Name", roomRate.getName());
            System.out.printf("%-15s: %s\n", "Room Type", roomRate.getRoomType().getName());
            System.out.printf("%-15s: %s\n", "Rate Type", roomRate.getRateType());
            System.out.printf("%-15s: %.2f\n", "Rate Per Night", roomRate.getRatePerNight());

            // Only display start and end dates if the rate type is PEAK or PROMOTION
            RateType type = roomRate.getRateType();
            if (type.equals(RateType.PEAK) || type.equals(RateType.PROMOTION)) {
                System.out.printf("%-15s: %s\n", "Start Date", roomRate.getStartDate());
                System.out.printf("%-15s: %s\n", "End Date", roomRate.getEndDate());
            }
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Room Rate Not Found! Please try again.");
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void updateRoomRate() throws RoomTypeNotFoundException {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yy");
        System.out.print("Enter Room Rate ID: ");
        Long roomRateId = sc.nextLong();
        sc.nextLine();
        System.out.print("Enter New Room Rate Name: ");
        String name = sc.nextLine();
        System.out.print("Enter new Room Type Name: ");
        String roomTypeName = sc.nextLine();
        System.out.println("Select New Rate Type (1: PUBLISHED, 2. NORMAL, 3. PEAK, 4. PROMOTION): ");
        int choice = sc.nextInt();
        RateType type = RateType.PUBLISHED;


        if (choice == 1) {
            type = RateType.PUBLISHED;
        } else if (choice == 2) {
            type = RateType.NORMAL;
        } else if (choice == 3) {
            type = RateType.PEAK;
        } else if (choice == 4) {
            type = RateType.PROMOTION;
        }

        sc.nextLine();
        System.out.print("Enter new Rate Per Night: ");
        BigDecimal ratePerNight = sc.nextBigDecimal();
        sc.nextLine();

        Date start = null;
        Date end = null;

        if (choice == 3 || choice == 4) {
            try {
                System.out.print("Enter your Start Date (dd-mm-yyyy): ");
                start = inputDateFormat.parse(sc.nextLine().trim());
                System.out.print("Enter your End Date (dd-mm-yyyy): ");
                end = inputDateFormat.parse(sc.nextLine().trim());
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Please enter the date in dd-mm-yyyy format.");
                return; // Exit the method if parsing fails
            }
        }
        try {
            RoomType roomtype = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
            roomRateSessionBean.updateRoomRate(roomRateId, name, roomtype, type, ratePerNight, start, end);
            System.out.println("Room Rate Successfully updated!");
        }
        catch (RoomTypeNotFoundException ex) {
            System.out.println("Room Type Not Found! Please try again.");
        } 
    }

    
    private static void deleteRoomRate() {
        System.out.print("Enter Room Rate ID: ");
        Long roomRateId = sc.nextLong();
        roomRateSessionBean.deleteRoomRate(roomRateId);
        System.out.println("Room Rate Successfully Deleted!");
    }
    
    private static void viewAllRoomRates() {
        List<RoomRate> list = roomRateSessionBean.viewAllRoomRates();
    
        // Display headers with alignment
        System.out.printf("%-15s %-30s %-20s %-15s %-15s %-15s %-15s\n", 
                "Room Rate ID", "Room Rate Name", "Room Type", "Rate Type", "Rate Per Night", "Start Date", "End Date");

        // Display each room rate's details in aligned columns
        for (RoomRate roomRate : list) {
            // Conditional date display: only show Start and End dates if applicable
            if (roomRate.getRateType() == RateType.PEAK || roomRate.getRateType() == RateType.PROMOTION) {
                System.out.printf("%-15d %-30s %-20s %-15s %-15.2f %-15s %-15s\n",
                        roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().getName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), 
                        roomRate.getStartDate(), roomRate.getEndDate());
            } else {
                System.out.printf("%-15d %-30s %-20s %-15s %-15.2f %-15s %-15s\n",
                        roomRate.getRoomRateId(), roomRate.getName(), roomRate.getRoomType().getName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), 
                        "-", "-"); // Use "-" if no dates are available
            }
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    // SYSTEM ADMIN METHODS
    
    private static void createNewEmployee() {
    try {
        System.out.print("Enter New Employee Username: ");
        String username = sc.nextLine();
        System.out.print("Enter New Employee Password: ");
        String password = sc.nextLine();
        System.out.print("Select New Employee Role (1: OPERATION MANAGER, 2: SALES MANAGER, 3: GUEST RELATION OFFICER, 4: SYSTEM ADMINISTRATOR): ");
        int choice = sc.nextInt();
        sc.nextLine();
        
        Long employeeId;

        // Determine role based on user input
        if (choice == 1) {
            employeeId = createEmployeeSessionBean.createOperationManager(username, password);
        } else if (choice == 2) {
            employeeId = createEmployeeSessionBean.createSalesManager(username, password);
        } else if (choice == 3) {
            employeeId = createEmployeeSessionBean.createGRO(username, password);
        } else {
            employeeId = createEmployeeSessionBean.createSystemAdmin(username, password);
        }
        
        // Fetch and print created employee details
        Employee employee = createEmployeeSessionBean.findEmployeeById(employeeId);
        System.out.println("Employee Successfully Created! Employee ID: " + employeeId + " Employee Role: " + employee.getStatus());
        
    } catch (EmployeeAlreadyExistsException ex) {
        System.out.println("Error: An employee with this username already exists. Please try a different username.");
    }
}

    
    private static void viewAllEmployees() {
        List<Employee> list = createEmployeeSessionBean.viewAllEmployees();

        // Display headers with alignment
        System.out.printf("%-15s %-20s %-20s %-15s\n", "Employee ID", "Username", "Password", "Role");

        // Display each employee's details in aligned columns
        for (Employee e : list) {
            System.out.printf("%-15d %-20s %-20s %-15s\n",
                    e.getEmployeeId(), e.getUsername(), e.getPassword(), e.getStatus());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void createNewPartner() {
        System.out.print("Enter New Partner Username: ");
        String username = sc.nextLine();
        System.out.print("Enter New Partner Password: ");
        String password = sc.nextLine();
        Long partnerId = createPartnerSessionBean.createPartner(username, password);
        System.out.println("New Partner Successfully Created! Partner ID: " + partnerId);
    }
    
    private static void viewAllPartners() {
        List<Partner> list = createPartnerSessionBean.viewAllPartners();

        // Display headers with alignment
        System.out.printf("%-15s %-20s %-20s\n", "Partner ID", "Username", "Password");

        // Display each partner's details in aligned columns
        for (Partner p : list) {
            System.out.printf("%-15d %-20s %-20s\n",
                    p.getPartnerId(), p.getUsername(), p.getPassword());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    // GUEST RELATION OFFICER METHODS
    
    private static void walkInSearchRoom() {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yy");
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            Date checkInDate = inputDateFormat.parse(sc.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            Date checkOutDate = inputDateFormat.parse(sc.nextLine());

            List<RoomType> availableRooms = createReservationSessionBean.searchAvailableRooms(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms for the selected dates.");
            } else {
                System.out.println("Available room types:");
                for (RoomType roomType : availableRooms) {
                    System.out.println("- " + roomType.getName() + "- Room Type ID: "+ roomType.getRoomTypeId());
                }
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
        }
    }
    
    private static void walkInReserveRoom() {
        System.out.print("Enter Guest First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Guest Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Enter Guest Phone Number: ");
        String phoneNumber = sc.nextLine();

        Date checkInDate;
        Date checkOutDate;

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            // Prompt for check-in and check-out dates
            System.out.print("Enter check-in date (DD-MM-YYYY): ");
            checkInDate = inputDateFormat.parse(sc.nextLine());

            System.out.print("Enter check-out date (DD-MM-YYYY): ");
            checkOutDate = inputDateFormat.parse(sc.nextLine());

            // Prompt for Room Type ID
            System.out.print("Enter Room Type ID: ");
            Long roomTypeId = Long.parseLong(sc.nextLine().trim());

            // Prompt for number of rooms
            System.out.print("Enter number of rooms to reserve: ");
            int numberOfRooms = Integer.parseInt(sc.nextLine().trim());

            // Call the reservation method
            Reservation reservation = createReservationSessionBean.walkInReserveRoom(
                    firstName, lastName, phoneNumber, roomTypeId, numberOfRooms, checkInDate, checkOutDate);

            // Print confirmation of the reservation
            System.out.println("Reservation created successfully!");
            System.out.println("Reservation ID: " + reservation.getReservationId());
            System.out.println("Total cost: " + reservation.getTotalCost());
            System.out.println("Status: " + reservation.getStatus());

        } catch (ParseException ex) {
            System.out.println("Invalid date format. Please enter the date in DD-MM-YYYY format.");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: Room Type not found. " + ex.getMessage());
        } catch (RoomTypeUnavailableException ex) {
            System.out.println("Error: Room Type is unavailable for the selected dates. " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input. Please enter numeric values for Room Type ID and number of rooms.");
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }
    
    // Method for checking in a guest
  private static void checkInGuest() throws ReservationNotFoundException, Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Reservation ID for check-in: ");
    Long reservationId = scanner.nextLong();

    try {
        // Retrieve the reservation by ID
        Reservation reservation = createReservationSessionBean.findReservationById(reservationId);

        // Check if the reservation is in a state that allows check-in
        if (reservation.getStatus() == Reservation.ReservationStatus.PENDING) {
            // Call the allocation session bean to allocate rooms for the reservation's check-in date
            allocatingRoomSessionBean.allocateRoomForReservation(reservation.getCheckInDate());

            // Refresh reservation to get updated room allocations
            reservation = createReservationSessionBean.findReservationById(reservationId);

            // Check if rooms were allocated successfully
            if (!reservation.getRooms().isEmpty()) {
                // Change status to CHECKED_IN
                createReservationSessionBean.updateReservationStatus(reservationId, Reservation.ReservationStatus.CHECKED_IN);
                System.out.println("Guest successfully checked in. Room(s) allocated.");


                // Display allocated rooms for confirmation
                System.out.println("Allocated Room(s): ");
                for (Room room : reservation.getRooms()) {
                    System.out.println("- Room Number: " + room.getRoomNumber());
                }
            } else {
                System.out.println("Check-in failed: No rooms were allocated. Please handle manually.");
            }

        } 
    } catch (ReservationNotFoundException ex) {
        System.out.println("Check-in failed: " + ex.getMessage());
    }
}


// Check-Out Guest Method
private static void checkOutGuest() throws ReservationNotFoundException, Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Reservation ID for check-out: ");
    Long reservationId = scanner.nextLong();

    try {
        // Retrieve the reservation by ID
        Reservation reservation = createReservationSessionBean.findReservationById(reservationId);

        // Check if the reservation is in a state that allows check-out
        if (reservation.getStatus() == Reservation.ReservationStatus.CHECKED_IN) {
            // Attempt to check out the guest
            createReservationSessionBean.updateReservationStatus(reservationId, Reservation.ReservationStatus.CHECKED_OUT);
            System.out.println("Guest successfully checked Out. Thank you for visiting!");

        } 
    } catch (ReservationNotFoundException ex) {
        System.out.println("Check-out failed: " + ex.getMessage());
    }
}

}   

    
    
    
      

