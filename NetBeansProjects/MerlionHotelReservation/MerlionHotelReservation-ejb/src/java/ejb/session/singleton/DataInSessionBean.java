package ejb.session.singleton;

import ejb.session.stateless.CreateEmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
import entity.SystemAdministrator;
import entity.OperationManager;
import entity.SalesManager;
import entity.GuestRelationOfficer;
import entity.RoomType;
import entity.RoomRate;
import entity.Room;
import entity.RoomRate.RateType;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import javax.ejb.EJB;
import util.exception.EmployeeAlreadyExistsException;
import util.exception.RoomTypeNotFoundException;

@Singleton
@Startup
public class DataInSessionBean {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private CreateEmployeeSessionBeanLocal createEmployeeSessionBean;
    
    
    
    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    // RoomType variables to hold references
    private RoomType deluxeRoomType;
    private RoomType premierRoomType;
    private RoomType familyRoomType;
    private RoomType juniorSuiteType;
    private RoomType grandSuiteType;

    @PostConstruct
    public void initializeData() {
        if(em.find(Employee.class, 1l) == null) {
            initializeEmployees();
            initializeRoomTypes();
            initializeRoomRates();
            initializeRooms();
        } 
    }

    private void initializeEmployees() {
        try {
            createEmployeeSessionBean.createOperationManager("opmanager", "password");
            createEmployeeSessionBean.createGRO("guestrelo", "password");
            createEmployeeSessionBean.createSystemAdmin("sysadmin", "password");
            createEmployeeSessionBean.createSalesManager("salesmanager", "password");
        } catch(EmployeeAlreadyExistsException ex) {
             System.out.println("Employee Already Exists");
        } 
    }

    private void initializeRoomTypes() {
      
        roomTypeSessionBean.createRoomType("Deluxe Room", "Deluxe Room", 4, 1, 2, "Basic", "Premier Room");
        roomTypeSessionBean.createRoomType("Premier Room", "Premier Room", 4, 2, 3, "Intermediate", "Family Room");
        roomTypeSessionBean.createRoomType("Family Room", "Family Room", 5, 2, 4, "Good", "Junior Suite");
        roomTypeSessionBean.createRoomType("Junior Suite", "Junior Suite", 5, 3, 4, "Great", "Grand Suite");            roomTypeSessionBean.createRoomType("Grand Suite", "Grand Suite", 5, 4, 4, "Excellent", "None");
      
    }

    private void initializeRoomRates() {
        roomRateSessionBean.createRoomRate("Deluxe Room Published", "Deluxe Room", RateType.PUBLISHED, new BigDecimal(100), null, null);
        roomRateSessionBean.createRoomRate("Deluxe Room Normal", "Deluxe Room", RateType.NORMAL, new BigDecimal(50), null, null);
        roomRateSessionBean.createRoomRate("Premier Room Published", "Premier Room", RateType.PUBLISHED, new BigDecimal(200), null, null);
        roomRateSessionBean.createRoomRate("Premier Room Normal", "Premier Room", RateType.NORMAL, new BigDecimal(100), null, null);
        roomRateSessionBean.createRoomRate("Family Room Published", "Family Room", RateType.PUBLISHED, new BigDecimal(300), null, null);
        roomRateSessionBean.createRoomRate("Family Room Normal", "Family Room", RateType.NORMAL, new BigDecimal(150), null, null);
        roomRateSessionBean.createRoomRate("Junior Suite Published", "Junior Suite", RateType.PUBLISHED, new BigDecimal(400), null, null);
        roomRateSessionBean.createRoomRate("Junior Suite Normal", "Junior Suite", RateType.NORMAL, new BigDecimal(200), null, null);
        roomRateSessionBean.createRoomRate("Grand Suite Published", "Grand Suite", RateType.PUBLISHED, new BigDecimal(500), null, null);
        roomRateSessionBean.createRoomRate("Grand Suite Normal", "Grand Suite", RateType.NORMAL, new BigDecimal(250), null, null);
    }

    private void initializeRooms() {
        
        roomSessionBean.createRoom("0101", "Deluxe Room");
        roomSessionBean.createRoom("0201", "Deluxe Room");
        roomSessionBean.createRoom("0301", "Deluxe Room");
        roomSessionBean.createRoom("0401", "Deluxe Room");
        roomSessionBean.createRoom("0501", "Deluxe Room");
        roomSessionBean.createRoom("0102", "Premier Room");
        roomSessionBean.createRoom("0202", "Premier Room");
        roomSessionBean.createRoom("0302", "Premier Room");
        roomSessionBean.createRoom("0402", "Premier Room");
        roomSessionBean.createRoom("0502", "Premier Room");
        roomSessionBean.createRoom("0103", "Family Room");
        roomSessionBean.createRoom("0203", "Family Room");
        roomSessionBean.createRoom("0303", "Family Room");
        roomSessionBean.createRoom("0403", "Family Room");
        roomSessionBean.createRoom("0503", "Family Room");
        roomSessionBean.createRoom("0104", "Junior Suite");
        roomSessionBean.createRoom("0204", "Junior Suite");
        roomSessionBean.createRoom("0304", "Junior Suite");
        roomSessionBean.createRoom("0404", "Junior Suite");
        roomSessionBean.createRoom("0504", "Junior Suite");
        roomSessionBean.createRoom("0105", "Grand Suite");
        roomSessionBean.createRoom("0205", "Grand Suite");
        roomSessionBean.createRoom("0305", "Grand Suite");
        roomSessionBean.createRoom("0405", "Grand Suite");
        roomSessionBean.createRoom("0505", "Grand Suite");
        
    }
}


