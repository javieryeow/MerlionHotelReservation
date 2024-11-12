/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package merlionhotelmanagementclient;

import ejb.session.stateless.CreateCustomerSessionBeanRemote;
import ejb.session.stateless.CreateEmployeeSessionBeanRemote;
import ejb.session.stateless.CreatePartnerSessionBeanRemote;
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

/**
 *
 * @author javieryeow
 */
public class Main {

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBean;

    @EJB
    private static EmployeeLoginRemote employeeLogin;

    @EJB
    private static CreatePartnerSessionBeanRemote createPartnerSessionBean;

    @EJB
    private static CreateEmployeeSessionBeanRemote createEmployeeSessionBean;

    @EJB
    private static CreateCustomerSessionBeanRemote createCustomerSessionBean;
    
    private static Scanner sc = new Scanner(System.in);
     
    public static void main(String[] args) {
        startManagementClient();
    }
    
    public static void startManagementClient() {
        System.out.print("Enter Employee Username: ");
        String username = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();
        
        Employee employee = employeeLogin.login(username, password);
        if (employee != null) {
            System.out.println("Login successful! Welcome, " + employee.getEmployeeId() + ".");
            if (employee.getStatus() == role.OPERATION_MANAGER) {
                operationManagerMenu();
            } else if (employee.getStatus() == role.GUEST_RELATIONS_OFFICER) {
                guestRelationOfficerMenu();
            } else if (employee.getStatus() == role.SALES_MANAGER) {
                salesManagerMenu();
            } else {
                systemAdminMenu();
            }
        } else {
            System.out.println("Invalid Employee username or Password. Please try again.");
        }
    }
    
    private static void operationManagerMenu() {
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
        System.out.println("11. Logout");

        int choice = sc.nextInt();
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
                System.out.println("Logging out..."); 
                return;
            default: System.out.println("Invalid choice");
        }
    }
    
    private static void guestRelationOfficerMenu() {
        System.out.println("1. Walk-in Search Room");
        System.out.println("2. Walk-in Reserve Room");
        System.out.println("3. Check-in Guest");
        System.out.println("4. Check-out Guest");
        System.out.println("5. Logout");

        int choice = sc.nextInt();
        switch (choice) {
            case 1: /* Search Room logic */ break;
            case 2: /* Reserve Room logic */ break;
            case 3: /* Check-in Guest logic */ break;
            case 4: /* Check-out Guest logic */ break;
            case 5:
                System.out.println("Logging out..."); 
                return;
            default: System.out.println("Invalid choice");
        }
    }
    
    private static void salesManagerMenu() {
        System.out.println("1. Create new Room Rate");
        System.out.println("2. View Room Rate Details");
        System.out.println("3. Update Room Rate");
        System.out.println("4. Delete Room Rate");
        System.out.println("5. View All Room Rates");
        System.out.println("6. Logout");
        
        int choice = sc.nextInt();
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
    
    private static void systemAdminMenu() {
        System.out.println("1. Create New Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Create New Partner");
        System.out.println("4. View All Partners");
        System.out.println("5. Logout");
        
        int choice = sc.nextInt();
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
    
    private static void createNewRoomType() {
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
        Long roomTypeId = roomTypeSessionBean.createRoomType(name, description, size, bed, capacity, amenities);
        System.out.println("Room Type successfully created! Room Type ID: " + roomTypeId);
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
        System.out.printf("RoomType ID", "Name", "Description", "Size", "Beds", "Capacity", "Amenities");
        for (RoomType roomtype: list) {
            System.out.printf("%s%s%s%s%s%s\n", roomtype.getRoomTypeId(), roomtype.getName(), roomtype.getDescription(), roomtype.getSize(), roomtype.getBed(), roomtype.getCapacity(), roomtype.getAmenities());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
    
    private static void viewRoomTypeDetails() {
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();
        RoomType roomtype = roomTypeSessionBean.findRoomTypeById(roomTypeId);
        System.out.printf("%s%s%s%s%s%s\n", roomtype.getRoomTypeId(), roomtype.getName(), roomtype.getDescription(), roomtype.getSize(), roomtype.getBed(), roomtype.getCapacity(), roomtype.getAmenities());
    }
    
    private static void createNewRoom() {
        System.out.print("Enter Room Type Name: ");
        String roomTypeName = sc.nextLine();
        System.out.println("Enter Room number: ");
        String roomNumber = sc.nextLine();
        
        RoomType roomtype = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
        Long roomId = roomSessionBean.createRoom(roomNumber, roomtype);
        System.out.println("Room created successfully! Room ID: " + roomId);
    }
    
    private static void updateRoom() {
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
        RoomType roomType = roomTypeSessionBean.findRoomTypeByName(roomTypeName);
        roomSessionBean.updateRoom(roomId, roomNumber, roomType, status);
        System.out.println("Room Successfully Updated!");
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
    }
    
    private static void viewRoomAllocationExceptionReport() {
        
    }
    
    private static void createNewRoomRate() {
        
    }
    
    
    
    
    
    
    
}
