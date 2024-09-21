package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.CompanyDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dto.CompanyDto;
import net.andreabattista.InOutFlow.dto.MessageDto;
import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.NfcReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for handling operations related to companies.
 * Utilizes the CompanyDao for database interactions and logs important actions.
 */
public class CompanyManager {

    private static final Logger LOG = LogManager.getLogger(CompanyManager.class);

    public static List<CompanyDto> getAll() {

        try {
            CompanyDao companyDao = new CompanyDao();

            List<Company> companies = companyDao.getAll(Company.class);
            List<CompanyDto> dto = new ArrayList<>();

            if (companies == null) {
                LOG.info("No companies found");
                throw new ValidationException("No companies found");
            }

            LOG.info("All companies saved in ArrayList");

            for (Company company : companies) {
                CompanyDto c = new CompanyDto();
                c.setBusinessName(company.getBusinessName());
                c.setOfficeAddress(company.getOfficeAddress());
                c.setPhoneNumber(company.getPhoneNumber());
                c.setEmailAddress(company.getEmailAddress());
                dto.add(c);
            }

            LOG.info("All companies retrieved from database");
            return dto;
        } catch(Exception e) {
            LOG.warn("Error while getting all companies", e);
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Retrieves the company information associated with the logged-in employee.
     *
     * @param token the login token of the employee
     * @return a CompanyDto object containing the company's business name, office address, phone number, and email address
     * @throws ValidationException if the employee or company is not found, or if an error occurs while retrieving the information
     */
    public static CompanyDto getByLoggedEmployee(String token) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            CompanyDao companyDao = new CompanyDao();

            Employee employee = employeeDao.getByLoginToken(token);

            if (employee == null) {
                LOG.warn("Employee not found");
                throw new ValidationException("Employee not found");
            }

            LOG.info("Found employee {}", employee);

            Company company = companyDao.getByEmployee(employee);

            if (company == null) {
                LOG.warn("Company not found");
                throw new ValidationException("Company not found");
            }

            LOG.info("Found company {}", company);

            CompanyDto c = new CompanyDto();
            c.setBusinessName(company.getBusinessName());
            c.setOfficeAddress(company.getOfficeAddress());
            c.setPhoneNumber(company.getPhoneNumber());
            c.setEmailAddress(company.getEmailAddress());

            return c;
        } catch (Exception e) {
            LOG.warn("Error while getting company by logged employee", e);
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Creates a new company in the database from the provided CompanyDto object.
     * Verifies the CompanyDto, converts it to a Company entity, and saves it to the database.
     * If the company is not saved successfully, a ValidationException is thrown.
     * Returns a list of all CompanyDto objects after the new company is saved.
     *
     * @param companyDto the CompanyDto object containing the details of the company to be created.
     * @return a list of CompanyDto objects representing all companies in the database.
     * @throws ValidationException if the company is not saved successfully.
     */
    public static List<CompanyDto> create(CompanyDto companyDto) {
        CompanyDao companyDao = new CompanyDao();
        verifyDto(companyDto);

        try {
            Company company = new Company();
            company.setBusinessName(companyDto.getBusinessName());
            company.setOfficeAddress(companyDto.getOfficeAddress());
            company.setEmailAddress(companyDto.getEmailAddress());
            company.setPhoneNumber(companyDto.getPhoneNumber());

            List<NfcReader> nfcReaders = NfcReaderManager.createFake();
            company.setNfcReaders(nfcReaders);

            companyDao.save(company);
            LOG.info("New company created");

            Company co = companyDao.getByEmailAddress(companyDto.getEmailAddress());
            if (co == null) {
                LOG.info("No company found with email address " + companyDto.getEmailAddress());
                throw new ValidationException("No company found with email address " + companyDto.getEmailAddress());
            }

            return getAll();
        } catch (Exception e) {
            LOG.warn("Error while creating company", e);
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Verifies the provided CompanyDto object by validating its fields.
     * Checks for the validity of the business name, office address, phone number, and email address.
     * Ensures that the business name, phone number, and email address are unique.
     * Throws a ValidationException if any validation fails.
     *
     * @param companyDto the CompanyDto object to be verified.
     * @throws ValidationException if any validation fails.
     */
    private static void verifyDto(CompanyDto companyDto) {
        CompanyDao companyDao = new CompanyDao();
        EmployeeDao employeeDao = new EmployeeDao();

        // validates company business name
        Company company = companyDao.getByBusinessName(companyDto.getBusinessName());
        if (companyDto.getBusinessName().isBlank() || companyDto.getBusinessName().isEmpty() || companyDto.getBusinessName() == null || company != null) {
            LOG.warn("Invalid company name");
            throw new ValidationException("Invalid business name");
        }

        // validates office address
        if (companyDto.getOfficeAddress().isBlank() || companyDto.getOfficeAddress().isEmpty() || companyDto.getOfficeAddress() == null) {
            LOG.warn("Invalid office address");
            throw new ValidationException("Invalid office address");
        }

        // validates phone number
        company = companyDao.getByPhoneNumber(companyDto.getPhoneNumber());
        Employee employee = employeeDao.getByPhoneNumber(companyDto.getPhoneNumber());
        if (companyDto.getPhoneNumber().isBlank() || companyDto.getPhoneNumber().isEmpty() || companyDto.getPhoneNumber() == null || companyDto.getPhoneNumber().length() != 10 || company != null || employee != null) {
            LOG.warn("Invalid phone number");
            throw new ValidationException("Invalid phone number");
        }

        // validates email address
        company = companyDao.getByEmailAddress(companyDto.getEmailAddress());
        employee = employeeDao.getByEmailAddress(companyDto.getEmailAddress());
        if (companyDto.getEmailAddress().isBlank() || companyDto.getEmailAddress().isEmpty() || companyDto.getEmailAddress() == null || company != null || employee != null) {
            LOG.warn("Invalid email address");
            throw new ValidationException("Invalid email address");
        }
    }

    /**
     * Deletes the companies specified in the provided list of CompanyDto objects from the database.
     * For each CompanyDto, it checks if the company exists in the database by email address.
     * If a company is not found, a ValidationException is thrown.
     * After deleting the specified companies, it returns a list of all remaining CompanyDto objects.
     *
     * @param companies the list of CompanyDto objects representing the companies to be deleted.
     * @return a list of CompanyDto objects representing all remaining companies in the database.
     * @throws ValidationException if any company in the list is not found in the database.
     */
    public static List<CompanyDto> delete(List<CompanyDto> companies) {
        CompanyDao companyDao = new CompanyDao();

        try {
            for (CompanyDto c : companies) {
                Company company = companyDao.getByEmailAddress(c.getEmailAddress());

                if (company == null) {
                    LOG.warn("No company found with email address " + c.getEmailAddress());
                    throw new ValidationException("Company not found");
                }

                companyDao.remove(company);
                LOG.info("Deleted company " + company.getBusinessName());
            }

            return getAll();
        } catch (Exception e) {
            LOG.warn("Error while deleting companies", e);
            throw new ValidationException(e.getMessage());
        }
    }

    /**
     * Modifies an existing company in the database based on the provided CompanyDto object.
     * Searches for the company by business name, phone number, or email address.
     * If the company is not found, a ValidationException is thrown.
     * Updates the company's details with the information from the CompanyDto and saves the changes.
     * Returns a list of all CompanyDto objects after the modification.
     *
     * @param companyDto the CompanyDto object containing the updated details of the company.
     * @return a list of CompanyDto objects representing all companies in the database.
     * @throws ValidationException if the company is not found in the database.
     */
    public static List<CompanyDto> modify(CompanyDto companyDto) {
        CompanyDao companyDao = new CompanyDao();

        try {
            Company company = companyDao.getByBusinessName(companyDto.getBusinessName());
            if (company == null) {
                company = companyDao.getByPhoneNumber(companyDto.getPhoneNumber());
                if (company == null) {
                    company = companyDao.getByEmailAddress(companyDto.getEmailAddress());
                    if (company == null) {
                        throw new ValidationException("Company not found");

                    }
                }
            }

            company.setBusinessName(companyDto.getBusinessName());
            company.setOfficeAddress(companyDto.getOfficeAddress());
            company.setPhoneNumber(companyDto.getPhoneNumber());
            company.setEmailAddress(companyDto.getEmailAddress());
            companyDao.update(company);
            LOG.info("Company modified");

            return getAll();
        } catch (Exception e) {
            LOG.warn("Error while modifying company", e);
            throw new ValidationException(e.getMessage());
        }
    }

    public static CompanyDto getByMessage(MessageDto messageDto) {
        try {
            CompanyDao companyDao = new CompanyDao();
            EmployeeDao employeeDao = new EmployeeDao();

            Employee employee = employeeDao.getByEmailAddress(messageDto.getSenderMail());

            if (employee == null) {
                throw new ValidationException("Employee not found");
            }

            Company company = companyDao.getByEmployee(employee);

            if (company == null) {
                throw new ValidationException("Company not found");
            }

            CompanyDto dto  = new CompanyDto();
            dto.setBusinessName(company.getBusinessName());
            dto.setPhoneNumber(company.getPhoneNumber());
            dto.setOfficeAddress(company.getOfficeAddress());
            dto.setEmailAddress(company.getEmailAddress());

            return dto;
        } catch (Exception e) {
            LOG.warn("Error while getting company", e);
            throw new ValidationException(e.getMessage());
        }
    }
}
