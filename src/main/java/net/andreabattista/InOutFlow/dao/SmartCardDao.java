package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.SmartCard;

import javax.persistence.NoResultException;

/**
 * Accesses and manipulates smart card entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the SmartCard entity.
 *
 * @author bullbatti
 */
public class SmartCardDao extends BaseDao {
    
    public SmartCard getByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("SmartCard.getByUUID", SmartCard.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Long countByUUID(String uuid) {
        return entityManager.createNamedQuery("SmartCard.countByUUID", Long.class)
            .setParameter("uuid", uuid)
            .getSingleResult();
    }
    
    public SmartCard getByEmployeeEmailAddress(String emailAddress) {
        try {
            return entityManager.createNamedQuery("SmartCard.getByEmployeeEmailAddress", SmartCard.class)
                .setParameter("emailAddress", emailAddress)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
