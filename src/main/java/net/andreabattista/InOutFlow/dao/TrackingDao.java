package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Tracking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Accesses and manipulates tracking entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the Tracking entity.
 *
 * @author bullbatti
 */
public class TrackingDao extends BaseDao{
    
    public List<Tracking> getByEmployeeAndDate(Employee employee, LocalDateTime date) {
        LocalDateTime endDate = date.plusDays(1);
        
        return entityManager.createNamedQuery("Tracking.getByEmployeeAndDate", Tracking.class)
            .setParameter("employee", employee)
            .setParameter("startDate", Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
            .setParameter("endDate", Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
            .getResultList();
    }
    
    public Long countByEmployeeAndDate(Employee employee, LocalDateTime date) {
        LocalDateTime endDate = date.plusDays(1);
        
        return entityManager.createNamedQuery("Tracking.countByEmployeeAndDate", Long.class)
            .setParameter("employee", employee)
            .setParameter("startDate", Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
            .setParameter("endDate", Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
            .getSingleResult();
    }
    
    public Long countLastMinute(Employee employee) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMinutes(1);
        
        return entityManager.createNamedQuery("Tracking.countByEmployeeAndDate", Long.class)
            .setParameter("employee", employee)
            .setParameter("startDate", Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()))
            .setParameter("endDate", Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))
            .getSingleResult();
    }

    public Tracking getByEmployeeAndStartDate(Employee employee, LocalDateTime date) {
        try {
            return entityManager.createNamedQuery("Tracking.getByEmployeeAndStartDate", Tracking.class)
                    .setParameter("employee", employee)
                    .setParameter("startDate", Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
