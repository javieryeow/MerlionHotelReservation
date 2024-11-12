import entity.Employee;
import entity.GuestRelationOfficer;
import entity.OperationManager;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import entity.SalesManager;
import entity.SystemAdministrator;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Singleton
@Startup
public class DataInSessionBean {

    @PersistenceContext(unitName = "MerlionHotelReservation-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        initializeEmployees();
        initializeRoomTypes();
        initializeRoomRates();
        initializeRooms();
    }

    private void initializeEmployees() {
        if (em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult() == 0) {
            em.persist(new SystemAdministrator("sysadmin", "password", Employee.role.SYSTEM_ADMIN));
            em.persist(new OperationManager("opmanager", "password", Employee.role.OPERATION_MANAGER));
            em.persist(new SalesManager("salesmanager", "password", Employee.role.SALES_MANAGER));
            em.persist(new GuestRelationOfficer("GROfficer", "password", Employee.role.GUEST_RELATIONS_OFFICER));
            em.flush();
            System.out.println("Employees initialized.");
        }
    }

    private void initializeRoomTypes() {
        if (em.createQuery("SELECT COUNT(rt) FROM RoomType rt", Long.class).getSingleResult() == 0) {
            RoomType deluxeRoom = new RoomType("Deluxe Room", "Spacious deluxe room", 300, 1, 2, Arrays.asList("WiFi", "TV"));
            RoomType premierRoom = new RoomType("Premier Room", "Elegant premier room", 400, 1, 2, Arrays.asList("WiFi", "TV", "Mini Bar"));
            RoomType familyRoom = new RoomType("Family Room", "Large family room", 500, 2, 4, Arrays.asList("WiFi", "TV", "Mini Bar", "Sofa Bed"));
            RoomType juniorSuite = new RoomType("Junior Suite", "Luxurious junior suite", 600, 1, 3, Arrays.asList("WiFi", "TV", "Mini Bar", "Sofa Bed", "Balcony"));
            RoomType grandSuite = new RoomType("Grand Suite", "Grand luxury suite", 800, 2, 5, Arrays.asList("WiFi", "TV", "Mini Bar", "Jacuzzi"));

            em.persist(deluxeRoom);
            em.persist(premierRoom);
            em.persist(familyRoom);
            em.persist(juniorSuite);
            em.persist(grandSuite);
            em.flush();
            System.out.println("Room Types initialized.");
        }
    }

    private void initializeRoomRates() {
        if (em.createQuery("SELECT COUNT(rr) FROM RoomRate rr", Long.class).getSingleResult() == 0) {
            RoomType deluxeRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Deluxe Room'", RoomType.class).getSingleResult();
            RoomType premierRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Premier Room'", RoomType.class).getSingleResult();
            RoomType familyRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Family Room'", RoomType.class).getSingleResult();
            RoomType juniorSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Junior Suite'", RoomType.class).getSingleResult();
            RoomType grandSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Grand Suite'", RoomType.class).getSingleResult();

            em.persist(new RoomRate("Deluxe Room Published", deluxeRoom, RoomRate.RateType.PUBLISHED, new BigDecimal("100"), null, null));
            em.persist(new RoomRate("Deluxe Room Normal", deluxeRoom, RoomRate.RateType.NORMAL, new BigDecimal("50"), null, null));
            em.persist(new RoomRate("Premier Room Published", premierRoom, RoomRate.RateType.PUBLISHED, new BigDecimal("200"), null, null));
            em.persist(new RoomRate("Premier Room Normal", premierRoom, RoomRate.RateType.NORMAL, new BigDecimal("100"), null, null));
            em.persist(new RoomRate("Family Room Published", familyRoom, RoomRate.RateType.PUBLISHED, new BigDecimal("300"), null, null));
            em.persist(new RoomRate("Family Room Normal", familyRoom, RoomRate.RateType.NORMAL, new BigDecimal("150"), null, null));
            em.persist(new RoomRate("Junior Suite Published", juniorSuite, RoomRate.RateType.PUBLISHED, new BigDecimal("400"), null, null));
            em.persist(new RoomRate("Junior Suite Normal", juniorSuite, RoomRate.RateType.NORMAL, new BigDecimal("200"), null, null));
            em.persist(new RoomRate("Grand Suite Published", grandSuite, RoomRate.RateType.PUBLISHED, new BigDecimal("500"), null, null));
            em.persist(new RoomRate("Grand Suite Normal", grandSuite, RoomRate.RateType.NORMAL, new BigDecimal("250"), null, null));
            em.flush();
            System.out.println("Room Rates initialized.");
        }
    }

    private void initializeRooms() {
        if (em.createQuery("SELECT COUNT(r) FROM Room r", Long.class).getSingleResult() == 0) {
            RoomType deluxeRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Deluxe Room'", RoomType.class).getSingleResult();
            RoomType premierRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Premier Room'", RoomType.class).getSingleResult();
            RoomType familyRoom = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Family Room'", RoomType.class).getSingleResult();
            RoomType juniorSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Junior Suite'", RoomType.class).getSingleResult();
            RoomType grandSuite = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = 'Grand Suite'", RoomType.class).getSingleResult();

            String[][] roomData = {
                {"0101", deluxeRoom}, {"0201", deluxeRoom}, {"0301", deluxeRoom}, {"0401", deluxeRoom}, {"0501", deluxeRoom},
                {"0102", premierRoom}, {"0202", premierRoom}, {"0302", premierRoom}, {"0402", premierRoom}, {"0502", premierRoom},
                {"0103", familyRoom}, {"0203", familyRoom}, {"0303", familyRoom}, {"0403", familyRoom}, {"0503", familyRoom},
                {"0104", juniorSuite}, {"0204", juniorSuite}, {"0304", juniorSuite}, {"0404", juniorSuite}, {"0504", juniorSuite},
                {"0105", grandSuite}, {"0205", grandSuite}, {"0305", grandSuite}, {"0405", grandSuite}, {"0505", grandSuite},
            };

            for (String[] room : roomData) {
                em.persist(new Room(room[0], room[1]));
            }
            em.flush();
            System.out.println("Rooms initialized.");
        }
    }
}
