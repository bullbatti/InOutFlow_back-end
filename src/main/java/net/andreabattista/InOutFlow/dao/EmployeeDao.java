package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Accesses and manipulates employee entity.
 * Extends the functionality of {@link BaseDao} and provides specific methods to the Employee entity.
 *
 * @author bullbatti
 */
public class EmployeeDao extends BaseDao {
    private static final Logger log = LogManager.getLogger(LoginManager.class);
    
    
    /**
     * Retrieves an employee by their email address.
     * Queries the database to find an employee with the specified email address.
     * If an employee with the given email address exists, it returns the employee object.
     * If no employee is found with the provided email address, it returns null.
     *
     * @param emailAddress The email address of the employee to retrieve
     * @return The employee with the specific email address, or null if not found.
     */
    public Employee getByEmail(String emailAddress) {
        try {
            return entityManager.createNamedQuery("Employee.getByEmail", Employee.class)
                .setParameter("emailAddress", emailAddress)
                .getSingleResult();
        } catch(NoResultException e) {
            log.warn("No employee found");
            return null;
        }
    }
    
    /**
     * Retrieves an employee by their login token.
     * Queries the database to find an employee associated with the specified login token.
     * If an employee with the given token exists, it returns the employee object.
     * If no employee is found with the provided token, it returns null.
     *
     * @param token The login token associated with the employee to retrieve.
     * @return The employee associated with the specified login token, or null if not found.
     */
    public Employee getByLoginToken(String token) {
        try {
            return entityManager.createNamedQuery("Employee.getByLoginToken", Employee.class)
                .setParameter("token", token)
                .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    /**
     * //TODO
     *
     * @param businessName
     * @return
     */
    public List<Employee> getByCompany(String businessName) {
        return entityManager.createNamedQuery("Employee.getByCompany", Employee.class)
            .setParameter("businessName", businessName)
            .getResultList();
    }
}
