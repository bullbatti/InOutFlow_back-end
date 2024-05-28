package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.LoginDao;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.LoginDto;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Login;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles operations related to user authentication and authorization.
 *
 * @author bullbatti
 */
public class LoginManager {
    
    private static final Logger log = LogManager.getLogger(LoginManager.class);
    
    /**
     * Compares password given by the user in the login form with the hashed password saved in the database.
     *
     * @param insertedPassword Password given by the user.
     * @param hashedPassword Password stored in the database.
     * @return {@code true} if the passwords are equals
     *         {@code false} if passwords are different
     */
    public static boolean checkPassword(String insertedPassword, String hashedPassword) {
        return BCrypt.checkpw(insertedPassword, hashedPassword);
    }
    
    /**
     * Validates the credentials provided by the user
     *
     * @param dto The login credentials provided by the user
     */
    public static void validateCredentials(LoginDto dto) {
        log.trace("Starting verifying credentials");
        
        // check if credentials are null
        if (dto == null) {
            log.warn("Invalid credentials");
            throw new ValidationException("Invalid credentials");
        }
        log.info("credentials are valid");
        
        // check if email is null, an empty string or does not contain @ character
        String email = dto.getEmail();
        if (email == null || email.isEmpty()) {
            log.warn("Invalid email");
            throw new ValidationException("Invalid email");
        }
        log.info("email syntax is valid");
        
        // check if password is null or an empty string
        String password = dto.getPassword();
        if (password == null || password.isEmpty()) {
            log.warn("Invalid password");
            throw new ValidationException("Invalid password");
        }
        log.info("password syntax is valid");
        log.trace("Credentials verified");
    }
    
    
    /**
     * Verifies the credentials provided by the user.
     * If the credentials match a user in the database a token associated with the user is generated.
     *
     * @param dto The login credentials provided by the user
     * @return {@code token} if email and password match with user in the database
     *         {@code null} if email and password do not match with user in tha database
     */
    public static String login(LoginDto dto) {
        validateCredentials(dto);
        
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getByEmail(dto.getEmail());
            
            if (employee == null) {
                log.warn("Invalid email: {}", dto.getEmail());
                throw new ValidationException("Invalid email");
            } else {
                log.info("Email matched with an employee: {}", dto.getEmail());
            }
            
            boolean passwordMatch = checkPassword(dto.getPassword(), employee.getPassword());
            
            if (passwordMatch) {
                log.info("Password matched for employee: {}", dto.getEmail());
                
                String token = UUID.randomUUID().toString();
                log.trace("Token generated: {}", token);
                
                LoginDao loginDao = new LoginDao();
                
                Login login = new Login();
                login.setToken(token);
                login.setGenerationTime(LocalDateTime.now());
                login.setEmployee(employee);
                
                loginDao.save(login);
                log.info("Token saved in the database for employee: {}", dto.getEmail());
                
                return token;
            } else {
                log.warn("Invalid password for employee: {}", dto.getEmail());
                throw new ValidationException("Invalid password");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
    
    
    /**
     * Queries the database to find the employee associated to the token and return a dto
     *
     * @param token The authorization token associated to the user
     * @return dto user informations
     */
   public static EmployeeDto getUser(String token) {
       
       EmployeeDto dto = new EmployeeDto();
       
       try {
           
           LoginDao loginDao = new LoginDao();
           Login login = loginDao.getByToken(token);
           LocalDateTime expirationTime = login.getGenerationTime().plusMinutes(15);
           loginDao.closeEntityManager();
           
           if (login == null || LocalDateTime.now().isAfter(expirationTime)) {
               throw new ValidationException("Token expired");
           }
           
           EmployeeDao dao = new EmployeeDao();
           Employee employee = dao.getByLoginToken(token);
           dao.closeEntityManager();
           
           dto.setFirstName(employee.getFirstName());
           log.info("Employee name saved in DTO");
           dto.setLastName(employee.getLastName());
           log.info("Employee surname saved in DTO");
           dto.setRollNumber(employee.getRollNumber());
           log.info("Employee roll number saved in DTO");
           dto.setAccountType(employee.getAccountType().name());
           log.info("Employee account type saved in DTO");
       } catch (Exception e) {
           log.error("Error occurred while processing token: {}", e.getMessage());
           throw new ValidationException(e.getMessage());
       }
       
       return dto;
   }
}
