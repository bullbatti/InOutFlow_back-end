package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.CompanyDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.SmartCardDao;
import net.andreabattista.InOutFlow.dto.CompanyDto;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.EmployeeToInsertDto;
import net.andreabattista.InOutFlow.dto.MessageDto;
import net.andreabattista.InOutFlow.model.AccountType;
import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;

import net.andreabattista.InOutFlow.model.SmartCard;
import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles operations related to user employees.
 * Utilizes the EmployeeDao for database interactions and logs important actions.
 */
public class EmployeeManager {
    private static final Logger LOG = LogManager.getLogger(EmployeeManager.class);
    private static final Faker FAKER = new Faker();

    /**
     * Hashes the given password using the bcrypt hashing algorithm.
     *
     * @param password the plaintext password to be hashed.
     * @return a hashed representation of the input password.
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Retrieves an EmployeeDto object based on the provided login token.
     *
     * @param token the login token used to identify the employee
     * @return an EmployeeDto object containing the employee's details
     * @throws ValidationException if the employee with the given login token is not found
     */
    public static EmployeeDto getByLoginToken(String token) {
        EmployeeDto employeeDto = new EmployeeDto();
        
        try {
            Employee employee = getEmployeeByToken(token);

            employeeDto.setFirstName(employee.getFirstName());
            employeeDto.setLastName(employee.getLastName());
            employeeDto.setRollNumber(employee.getRollNumber());
            employeeDto.setAccountType(employee.getAccountType());
            employeeDto.setEmailAddress(employee.getEmailAddress());
            employeeDto.setImage(employee.getImage());
            employeeDto.setChangePassword(!employee.isPasswordChanged());
            LOG.info("Employee data saved in DTO");

            return employeeDto;
        } catch (Exception e) {
            LOG.warn("Unable to retrieve employee data: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Modifies the password of an employee identified by the provided login token.
     *
     * @param token the login token used to identify the employee
     * @param password the new password to be set for the employee
     * @return true if the password was successfully modified
     * @throws ValidationException if there is an error during the password modification process
     */
    public static boolean modifyPassword(String token, String password) {
        EmployeeDao employeeDao = new EmployeeDao();
        try {
            Employee employee = getEmployeeByToken(token);

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            employee.setPassword(hashedPassword);
            employee.setPasswordChanged(true);
            employeeDao.update(employee);

            Employee updatedEmployee = getEmployeeByToken(token);

            if (!LoginManager.checkPassword(password, updatedEmployee.getPassword())) {
                LOG.warn("Employee password doesn't match");
                throw new ValidationException("Employee password update error");
            }

            LOG.info("Employee password modified");
            return true;
        } catch (Exception e) {
            LOG.warn("Unable to modify password: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Retrieves a list of EmployeeDto objects for all employees in the same company as the employee identified by the provided login token.
     *
     * @param token the login token used to identify the employee
     * @return a list of EmployeeDto objects containing the details of the employees in the same company
     * @throws ValidationException if the company or employee data cannot be retrieved
     */
    public static List<EmployeeDto> getAllByLoggedUserCompany(String token) {
        try {
            Employee employee = getEmployeeByToken(token);

            CompanyDao companyDao = new CompanyDao();
            Company company = companyDao.getByEmployee(employee);

            List<Employee> employees = company.getEmployees();
            employees.remove(employee);
            LOG.info("Logged employee info removed from the list");

            if (company == null) {
                LOG.warn("Company not found");
                throw new ValidationException("Company not found");
            }

            LOG.info("Company found for employee id: {}", employee.getId());

            return getAllEmployeesDto(employees);
        } catch (Exception e) {
            LOG.warn("Unable to retrieve employee data for company the selected company: {}",  e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Retrieves a list of employees associated with the selected company.
     *
     * @param token the login token of the logged-in employee
     * @param companyDto the data transfer object containing the selected company's information
     * @return a list of EmployeeDto objects representing the employees of the selected company
     * @throws ValidationException if the logged-in employee, their company, or the selected company is not found
     */
    public static List<EmployeeDto> getAllBySelectedCompany(String token, CompanyDto companyDto) {
        CompanyDao companyDao = new CompanyDao();
        EmployeeDao employeeDao = new EmployeeDao();

        Employee loggedEmployee = employeeDao.getByLoginToken(token);

        if (loggedEmployee == null) {
            LOG.warn("Employee not found");
            throw new ValidationException("Employee not found");
        }

        Company loggedEmployeeCompany = companyDao.getByEmployee(loggedEmployee);

        if (loggedEmployeeCompany == null) {
            LOG.warn("Logged Employee's company not found");
            throw new ValidationException("Logged employee's company not found");
        }

        Company selectedCompany = companyDao.getByEmailAddress(companyDto.getEmailAddress());

        if (selectedCompany == null) {
            LOG.warn("Selected company not found");
            throw new ValidationException("Selected company not found");
        }

        List<Employee> employees = employeeDao.getByCompany(selectedCompany.getBusinessName());

        return getAllEmployeesDto(employees);
    }

    /**
     * Creates a new employee and returns a list of all employees in the selected company.
     *
     * @param token the login token of the logged-in employee
     * @param newEmployee the EmployeeToInsertDto object containing the new employee's details
     * @param companyDto the CompanyDto object containing the selected company's information
     * @return a list of EmployeeDto objects representing all employees in the selected company
     * @throws ValidationException if any validation checks fail or if an unexpected error occurs
     */
    public static List<EmployeeDto> create(String token, EmployeeToInsertDto newEmployee, CompanyDto companyDto) {
        try {
            verifyDto(newEmployee);

            CompanyDao companyDao = new CompanyDao();
            EmployeeDao employeeDao = new EmployeeDao();
            SmartCardDao smartCardDao = new SmartCardDao();

            Employee loggedEmployee = employeeDao.getByLoginToken(token);
            Company loggedEmployeeCompany = companyDao.getByEmployee(loggedEmployee);

            Company company = companyDao.getByEmailAddress(companyDto.getEmailAddress());

            if (company == null) {
                LOG.warn("Company not found");
                throw new ValidationException("Company not found");
            }

            Employee employee = new Employee();
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getLastName());

            employee.setBirthdate(LocalDate.parse(newEmployee.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            employee.setPhoneNumber(newEmployee.getPhoneNumber());
            employee.setEmailAddress(newEmployee.getEmailAddress());
            employee.setAccountType(AccountType.valueOf(newEmployee.getAccountType()));
            employee.setPasswordChanged(false);

            LocalDateTime dateTime = LocalDateTime.parse(newEmployee.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
            LocalDate date = dateTime.toLocalDate();
            employee.setBirthdate(date);

            String hashedPassword = hashPassword("password");
            employee.setPassword(hashedPassword);
            employee.setPasswordChanged(false);

            employee.setImage(FAKER.avatar().image());

            SmartCard smartCard = new SmartCard();
            smartCard.setUniversalId(newEmployee.getSmartCardNumber());
            smartCardDao.save(smartCard);
            employee.setSmartCard(smartCard);

            String rollNumber;
            do {
                rollNumber = String.valueOf(FAKER.number().numberBetween(11111, 99999));
            } while (employeeDao.getByRollNumber(rollNumber) != null);

            employee.setRollNumber(rollNumber);
            employeeDao.save(employee);

            List<Employee> employees = company.getEmployees();
            if (company.getEmailAddress().equals(loggedEmployeeCompany.getEmailAddress())) {
                employees.remove(loggedEmployee);
            }

            return getAllEmployeesDto(employees);
        } catch (Exception e) {
            throw new ValidationException("An unexpected error occurred: " + e.getMessage());
        }
   }

    /**
     * Validates the details of a new employee to be inserted.
     *
     * @param newEmployee the EmployeeToInsertDto object containing the new employee's details
     * @throws ValidationException if any of the validation checks fail, such as invalid first name, last name, date of birth, email address, account type, phone number, or smart card number
     */
    private static void verifyDto(EmployeeToInsertDto newEmployee) {

        LocalDateTime dateTime = LocalDateTime.parse(newEmployee.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
        LocalDate date = dateTime.toLocalDate();

        if (newEmployee.getFirstName() == null || newEmployee.getFirstName().isBlank() || newEmployee.getFirstName().isEmpty()) {
            LOG.warn("First name not valid");
            throw new ValidationException("Invalid First Name");
        }

        if (newEmployee.getLastName() == null || newEmployee.getLastName().isBlank() || newEmployee.getLastName().isEmpty()) {
            LOG.warn("Last name not valid");
            throw new ValidationException("Invalid Last Name");
        }

        if (date == null || date.isAfter(LocalDate.now().minusYears(18))) {
            LOG.warn("Date of birth not valid");
            throw new ValidationException("Invalid Date of birth");
        }

        if (newEmployee.getEmailAddress() == null || newEmployee.getEmailAddress().isBlank() || newEmployee.getEmailAddress().isEmpty()) {
            LOG.warn("Invalid email address");
            throw new ValidationException("Invalid email address");
        }

        EmployeeDao dao = new EmployeeDao();
        Employee employee = dao.getByEmailAddress(newEmployee.getEmailAddress());

        if(employee != null) {
            LOG.warn("Email already used");
            throw new ValidationException("Email already used");
        }

        if (newEmployee.getAccountType() == null || newEmployee.getAccountType().isBlank() || newEmployee.getAccountType().isEmpty() ||
        !newEmployee.getAccountType().equals(AccountType.USER.name()) ||
                !newEmployee.getAccountType().equals(AccountType.SUPPORT.name()) ||
                !newEmployee.getAccountType().equals(AccountType.ADMINISTRATOR.name())) {
            LOG.warn("Invalid account type");
            throw new ValidationException("Invalid account type");
        }

        if (newEmployee.getPhoneNumber() == null || newEmployee.getPhoneNumber().isBlank() || newEmployee.getPhoneNumber().isEmpty()) {
            LOG.warn("Invalid phone number");
            throw new ValidationException("Invalid phone number");
        }

        employee = dao.getByPhoneNumber(newEmployee.getPhoneNumber());

        if(employee != null) {
            LOG.warn("Phone number already used");
            throw new ValidationException("Phone number already used");
        }

        if (newEmployee.getSmartCardNumber() == null || newEmployee.getSmartCardNumber().isBlank() || newEmployee.getSmartCardNumber().isEmpty()) {
            LOG.warn("Invalid smart card number");
            throw new ValidationException("Invalid smart card number");
        }

        SmartCardDao smartCardDao = new SmartCardDao();
        SmartCard smartCard = smartCardDao.getByUUID(newEmployee.getSmartCardNumber());

        if (smartCard != null) {
            LOG.warn("SmartCard already used");
            throw new ValidationException("SmartCard already used");
        }
    }

    /**
     * Modifies the details of an existing employee and returns a list of all employees in the same company.
     *
     * @param employeeDto the EmployeeDto object containing the updated employee details
     * @return a list of EmployeeDto objects representing all employees in the company of the modified employee
     * @throws ValidationException if the employee or their company is not found
     */
    public static List<EmployeeDto> modifyEmployee(EmployeeDto employeeDto) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            CompanyDao companyDao = new CompanyDao();

            Employee employee = employeeDao.getByEmailAddress(employeeDto.getEmailAddress());
            if (employee == null) {
                employee = employeeDao.getByRollNumber(employeeDto.getRollNumber());
                if (employee == null) {
                    throw new ValidationException("Employee not found");
                }
            }

            Company company = companyDao.getByEmployee(employee);
            if (company == null) {
                throw new ValidationException("Company not found");
            }

            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setRollNumber(employeeDto.getRollNumber());
            employee.setAccountType(employeeDto.getAccountType());
            employee.setEmailAddress(employeeDto.getEmailAddress());
            employeeDao.update(employee);
            LOG.info("Employee data updated");

            return getAllEmployeesDto(company.getEmployees());
        } catch (Exception e) {
            LOG.warn("An unexpected error occurred: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Removes a list of employees from the company and returns the updated list of employees.
     *
     * @param employeeDto the list of EmployeeDto objects representing the employees to be removed
     * @return a list of EmployeeDto objects representing the remaining employees in the company
     * @throws ValidationException if any of the employees are not found or if an unexpected error occurs
     */
    public static List<EmployeeDto> remove(List<EmployeeDto> employeeDto) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            CompanyDao companyDao = new CompanyDao();

            Employee employee = employeeDao.getByEmailAddress(employeeDto.getFirst().getEmailAddress());
            Company company = companyDao.getByEmployee(employee);

            for (EmployeeDto e : employeeDto) {
                Employee employeeDb = employeeDao.getByEmailAddress(e.getEmailAddress());

                if (employeeDb == null) {
                    LOG.warn("Employee not found");
                    throw new ValidationException("Employee not found");
                }

                LOG.info("employee found: {}", employeeDb);

                company.getEmployees().remove(employeeDb);
                companyDao.save(company);
                employeeDao.remove(employeeDb);
            }

            return getAllEmployeesDto(company.getEmployees());
        } catch (Exception e) {
            LOG.warn("An unexpected error occurred: {}", e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Retrieves an employee based on their login token.
     *
     * @param token the login token of the employee
     * @return the Employee object associated with the given login token
     * @throws ValidationException if the employee with the given login token is not found
     */
    public static Employee getEmployeeByToken(String token) {
        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByLoginToken(token);

        if (employee == null) {
            LOG.warn("Employee with login token {} not found", token);
            throw new ValidationException("Employee with login token " + token + " not found");
        }

        LOG.info("Employee with login token {} found", token);
        return employee;
    }

    /**
     * Converts a list of Employee objects to a list of EmployeeDto objects.
     *
     * @param employees the list of Employee objects to be converted
     * @return a list of EmployeeDto objects containing the employee details
     */
    private static List<EmployeeDto> getAllEmployeesDto(List<Employee> employees) {
        List<EmployeeDto> dto = new ArrayList<>();

        for (Employee e : employees) {
            EmployeeDto eDto = new EmployeeDto();
            eDto.setFirstName(e.getFirstName());
            eDto.setLastName(e.getLastName());
            eDto.setAccountType(e.getAccountType());
            eDto.setEmailAddress(e.getEmailAddress());
            eDto.setRollNumber(e.getRollNumber());
            eDto.setChangePassword(!e.isPasswordChanged());

            dto.add(eDto);
        }

        return dto;
    }

    public static EmployeeDto getByMessage(MessageDto dto) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getByEmailAddress(dto.getSenderMail());

            if (employee == null) {
                throw new ValidationException("Employee not found");
            }

            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setFirstName(employee.getFirstName());
            employeeDto.setLastName(employee.getLastName());
            employeeDto.setAccountType(employee.getAccountType());
            employeeDto.setEmailAddress(employee.getEmailAddress());
            employeeDto.setRollNumber(employee.getRollNumber());
            employeeDto.setChangePassword(!employee.isPasswordChanged());
            employeeDto.setImage(employee.getImage());

            return employeeDto;
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
