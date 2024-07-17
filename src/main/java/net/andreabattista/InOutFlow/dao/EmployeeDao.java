package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.business.LoginManager;
import net.andreabattista.InOutFlow.dto.MessageDto;
import net.andreabattista.InOutFlow.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Data Access Object (DAO) class for Employee.
 * Provides methods to perform CRUD operations on Employee entities.
 */
public class EmployeeDao extends BaseDao {
    private static final Logger log = LogManager.getLogger(LoginManager.class);

    /**
     * Retrieves an employee based on their login token.
     *
     * @param token the login token of the employee
     * @return the Employee object associated with the given login token, or null if no employee is found
     */
    public Employee getByLoginToken(String token) {
        try {
            return entityManager.createNamedQuery("Employee.getByLoginToken", Employee.class)
                .setParameter("token", token)
                .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Retrieves an employee based on their email address.
     *
     * @param emailAddress the email address of the employee
     * @return the Employee object associated with the given email address, or null if no employee is found
     */
    public Employee getByEmailAddress(String emailAddress) {
        try {
            return entityManager.createNamedQuery("Employee.getByEmail", Employee.class)
                .setParameter("emailAddress", emailAddress)
                .getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Retrieves an employee based on their smart card.
     *
     * @param card the SmartCard object associated with the employee
     * @return the Employee object associated with the given smart card, or null if no employee is found
     */
    public Employee getBySmartCard(SmartCard card) {
        try {
            return entityManager.createNamedQuery("Employee.getBySmartCard", Employee.class)
                    .setParameter("smartCard", card)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a list of employees based on the company's business name.
     *
     * @param businessName the business name of the company
     * @return a list of Employee objects associated with the given business name, or null if no employees are found
     */
    public List<Employee> getByCompany(String businessName) {
        try {
            return entityManager.createNamedQuery("Employee.getByCompany", Employee.class)
                    .setParameter("businessName", businessName)
                    .getResultList();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Retrieves an employee based on their roll number.
     *
     * @param rollNumber the roll number of the employee
     * @return the Employee object associated with the given roll number, or null if no employee is found
     */
    public Employee getByRollNumber(String rollNumber) {
        try {
            return entityManager.createNamedQuery("Employee.getByRollNumber", Employee.class)
                    .setParameter("rollNumber", rollNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all support employees in a given company, excluding the sender.
     *
     * @param company the Company object representing the company
     * @param sender the Employee object representing the sender
     * @return a list of Employee objects with the account type USER in the specified company, or null if no support employees are found
     */
    public List<Employee> getAllCompanySupportEmployees(Company company, Employee sender) {
        try {
            return entityManager.createNamedQuery("Employee.getByCompanyAndSupportType", Employee.class)
                    .setParameter("company", company)
                    .setParameter("accountType", AccountType.USER)
                    .setParameter("sender", sender)
                    .getResultList();
        } catch(NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves a list of all administrators, excluding the sender.
     *
     * @param sender the Employee object representing the sender
     * @return a list of Employee objects with the account type ADMINISTRATOR, or null if no administrators are found
     */
    public List<Employee> getAllAdministrators(Employee sender) {
        try {
            return entityManager.createNamedQuery("Employee.getByAdministratorType", Employee.class)
                    .setParameter("accountType", AccountType.ADMINISTRATOR)
                    .setParameter("sender", sender)
                    .getResultList();
        } catch(NoResultException e) {
            return null;
        }
    }

    /**
     * Retrieves an employee based on their phone number.
     *
     * @param phoneNumber the phone number of the employee
     * @return the Employee object associated with the given phone number, or null if no employee is found
     */
    public Employee getByPhoneNumber(String phoneNumber) {
        try {
            return entityManager.createNamedQuery("Employee.getByPhoneNumber", Employee.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
