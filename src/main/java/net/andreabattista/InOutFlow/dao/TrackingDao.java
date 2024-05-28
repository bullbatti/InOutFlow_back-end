package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Tracking;

import java.util.List;

/**
 * Accesses and manipulates tracking entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the Tracking entity.
 *
 * @author bullbatti
 */
public class TrackingDao extends BaseDao{
    
    public List<Tracking> getByUser(Employee employee) {
        return entityManager.createNamedQuery("Tracking.getByEmployee", Tracking.class)
            .setParameter("employee", employee)
            .getResultList();
    }
}
