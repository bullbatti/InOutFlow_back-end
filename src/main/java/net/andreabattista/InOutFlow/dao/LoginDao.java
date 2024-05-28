package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.Login;

import javax.persistence.NoResultException;

/**
 * Accesses and manipulates login entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the Login entity.
 *
 * @author bullbatti
 */
public class LoginDao extends BaseDao{
    
    /**
     * TODO
     *
     * @param token
     * @return
     */
    public Login getByToken(String token) {
        try{
            return entityManager.createNamedQuery("Login.getByToken", Login.class)
                .setParameter("token", token)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
