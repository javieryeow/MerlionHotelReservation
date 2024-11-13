/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomRateSessionBeanRemote {
    
    public Long createRoomRate(String name, String roomTypeName, RoomRate.RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate);
    
    public RoomRate viewRoomRateDetails(Long roomRateId);

    public void updateRoomRate(Long roomRateId, String name, RoomType roomType, RoomRate.RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRate> viewAllRoomRates();
    
    public RoomRate findRoomRateById(Long roomRateId) throws RoomRateNotFoundException;
}
