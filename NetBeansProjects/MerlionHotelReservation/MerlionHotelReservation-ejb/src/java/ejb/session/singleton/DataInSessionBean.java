package ejb.session.singleton;

import entity.SystemAdministrator;
import entity.OperationManager;
import entity.SalesManager;
import entity.GuestRelationOfficer;
import entity.RoomType;
import entity.RoomRate;
import entity.Room;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Singleton
@Startup
public class DataInSessionBean {

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
        initializeEmployees();
        initializeRoomTypes();
        initializeRoomRates();
        initializeRooms();
    }

    private void initializeEmployees() {
        if (em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult() == 0) {
            em.persist(new SystemAdministrator("sysadmin", "password"));
            em.persist(new OperationManager("opmanager", "password"));
            em.persist(new SalesManager("salesmanager", "password"));
            em.persist(new GuestRelationOfficer("guestrelo", "password"));
            em.flush();
        }
    }

    private void initializeRoomTypes() {
        if (em.createQuery("SELECT COUNT(rt) FROM RoomType rt", Long.class).getSingleResult() == 0) {
            deluxeRoomType = new RoomType("Deluxe Room", "Next higher: Premier Room", 25, 1, 2, null);
            premierRoomType = new RoomType("Premier Room", "Next higher: Family Room", 30, 1, 3, null);
            familyRoomType = new RoomType("Family Room", "Next higher: Junior Suite", 35, 2, 4, null);
            juniorSuiteType = new RoomType("Junior Suite", "Next higher: Grand Suite", 40, 2, 4, null);
            grandSuiteType = new RoomType("Grand Suite", "Highest room type", 50, 3, 5, null);

            em.persist(deluxeRoomType);
            em.persist(premierRoomType);
            em.persist(familyRoomType);
            em.persist(juniorSuiteType);
            em.persist(grandSuiteType);
            em.flush();
        }
    }

    private void initializeRoomRates() {
        if (em.createQuery("SELECT COUNT(rr) FROM RoomRate rr", Long.class).getSingleResult() == 0) {
            em.persist(new RoomRate("Deluxe Room Published", deluxeRoomType, RoomRate.RateType.PUBLISHED, new BigDecimal("100"), null, null));
            em.persist(new RoomRate("Deluxe Room Normal", deluxeRoomType, RoomRate.RateType.NORMAL, new BigDecimal("50"), null, null));
            em.persist(new RoomRate("Premier Room Published", premierRoomType, RoomRate.RateType.PUBLISHED, new BigDecimal("200"), null, null));
            em.persist(new RoomRate("Premier Room Normal", premierRoomType, RoomRate.RateType.NORMAL, new BigDecimal("100"), null, null));
            em.persist(new RoomRate("Family Room Published", familyRoomType, RoomRate.RateType.PUBLISHED, new BigDecimal("300"), null, null));
            em.persist(new RoomRate("Family Room Normal", familyRoomType, RoomRate.RateType.NORMAL, new BigDecimal("150"), null, null));
            em.persist(new RoomRate("Junior Suite Published", juniorSuiteType, RoomRate.RateType.PUBLISHED, new BigDecimal("400"), null, null));
            em.persist(new RoomRate("Junior Suite Normal", juniorSuiteType, RoomRate.RateType.NORMAL, new BigDecimal("200"), null, null));
            em.persist(new RoomRate("Grand Suite Published", grandSuiteType, RoomRate.RateType.PUBLISHED, new BigDecimal("500"), null, null));
            em.persist(new RoomRate("Grand Suite Normal", grandSuiteType, RoomRate.RateType.NORMAL, new BigDecimal("250"), null, null));
            em.flush();
        }
    }

    private void initializeRooms() {
        if (em.createQuery("SELECT COUNT(r) FROM Room r", Long.class).getSingleResult() == 0) {
            em.persist(new Room("0101", deluxeRoomType));
            em.persist(new Room("0201", deluxeRoomType));
            em.persist(new Room("0301", deluxeRoomType));
            em.persist(new Room("0401", deluxeRoomType));
            em.persist(new Room("0501", deluxeRoomType));
            em.persist(new Room("0102", premierRoomType));
            em.persist(new Room("0202", premierRoomType));
            em.persist(new Room("0302", premierRoomType));
            em.persist(new Room("0402", premierRoomType));
            em.persist(new Room("0502", premierRoomType));
            em.persist(new Room("0103", familyRoomType));
            em.persist(new Room("0203", familyRoomType));
            em.persist(new Room("0303", familyRoomType));
            em.persist(new Room("0403", familyRoomType));
            em.persist(new Room("0503", familyRoomType));
            em.persist(new Room("0104", juniorSuiteType));
            em.persist(new Room("0204", juniorSuiteType));
            em.persist(new Room("0304", juniorSuiteType));
            em.persist(new Room("0404", juniorSuiteType));
            em.persist(new Room("0504", juniorSuiteType));
            em.persist(new Room("0105", grandSuiteType));
            em.persist(new Room("0205", grandSuiteType));
            em.persist(new Room("0305", grandSuiteType));
            em.persist(new Room("0405", grandSuiteType));
            em.persist(new Room("0505", grandSuiteType));
            em.flush();
        }
    }
}


