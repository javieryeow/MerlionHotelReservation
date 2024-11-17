/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author javieryeow
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createRoomRate(String name, String roomTypeName, RoomRate.RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomTypeNotFoundException;

    public RoomRate viewRoomRateDetails(Long roomRateId);

    public void updateRoomRate(Long roomRateId, String name, RoomType roomType, RoomRate.RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException;

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException;

    public List<RoomRate> viewAllRoomRates();

    public RoomRate findRoomRateById(Long roomRateId) throws RoomRateNotFoundException;

    public BigDecimal getReservationRate(RoomType roomType, Date date) throws RoomRateNotFoundException; 
    
}
