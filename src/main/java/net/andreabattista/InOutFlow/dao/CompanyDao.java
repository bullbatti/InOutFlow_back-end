package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;

import javax.persistence.NoResultException;


/**
 * Data Access Object (DAO) class for Company.
 * Provides methods to perform CRUD operations on Company entities.
 */
public class CompanyDao extends BaseDao {

    /**
     * Retrieves a Company entity associated with the given Employee.
     *
     * @param employee the Employee entity to search for.
     * @return the Company entity associated with the given Employee, or null if not found.
     */
    public Company getByEmployee(Employee employee) {
        try {
            return entityManager.createNamedQuery("Company.getByEmployee", Company.class)
                    .setParameter("employee", employee)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a Company entity by its email address.
     *
     * @param emailAddress the email address to search for.
     * @return the Company entity with the given email address, or null if not found.
     */
    public Company getByEmailAddress(String emailAddress) {
        try {
            return entityManager.createNamedQuery("Company.getByEmailAddress", Company.class)
                    .setParameter("emailAddress", emailAddress)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a Company entity by its business name.
     *
     * @param businessName the business name to search for.
     * @return the Company entity with the given business name, or null if not found.
     */
    public Company getByBusinessName(String businessName) {
        try {
            return entityManager.createNamedQuery("Company.getByBusinessName", Company.class)
                    .setParameter("businessName", businessName)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a Company entity by its phone number.
     *
     * @param phoneNumber the phone number to search for.
     * @return the Company entity with the given phone number, or null if not found.
     */
    public Company getByPhoneNumber(String phoneNumber) {
        try {
            return entityManager.createNamedQuery("Company.getByPhoneNumber", Company.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
