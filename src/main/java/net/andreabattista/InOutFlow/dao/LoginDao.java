package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.business.ValidationException;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Login;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Accesses and manipulates login entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the Login entity.
 *
 * @author bullbatti
 */
public class LoginDao extends BaseDao{
    
    /**
     * Retrieves a Login entity from the database using the provided token.
     * Attempts to find a Login entity based on the specified token.
     * If no matching entity is found, it returns {@code null}.
     *
     * @param token the token used to search for the Login entity
     * @return the Login entity associated with the provided token, or {@code null} if no such entity exists
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
