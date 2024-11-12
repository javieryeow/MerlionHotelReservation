/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author javieryeow
 */
@Remote
public interface RoomRateSessionBeanRemote {
    
    public Long createRoomRate(String name, RoomType roomType, RoomRate.RateType rateType, int ratePerNight, LocalDate startDate, LocalDate endDate);
    
    public RoomRate viewRoomRateDetails(Long roomRateId);

    public void updateRoomRate(Long roomRateId, String name, RoomType roomType, RoomRate.RateType rateType, int ratePerNight, LocalDate startDate, LocalDate endDate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRate> viewAllRoomRates();
}