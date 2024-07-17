package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.LoginDao;
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
     * Verifies the credentials provided by the employee.
     * If the credentials match an employee in the database a token associated with the employee is generated.
     *
     * @param loginDto The login credentials provided by the employee
     * @return {@code token} if email and password match with employee in the database
     *         {@code null} if email and password do not match with employee in tha database
     */
    public static String login(LoginDto loginDto) {
        validateCredentials(loginDto);
        
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getByEmailAddress(loginDto.getEmail());
            
            if (employee != null) {
                log.info("Employee found: {}", loginDto.getEmail());
            } else {
                log.warn("Employee not found: {}", loginDto.getEmail());
                throw new ValidationException("Employee not found");
            }
            
            boolean passwordMatch = checkPassword(loginDto.getPassword(), employee.getPassword());
            
            if (passwordMatch) {
                log.info("Correct password: {}", loginDto.getEmail());
                
                String token = UUID.randomUUID().toString();
                log.trace("Token generated: {}", token);

                Login login = new Login();
                login.setToken(token);
                login.setVersion(LocalDateTime.now());
                login.setEmployee(employee);
                
                LoginDao loginDao = new LoginDao();
                loginDao.save(login);
                
                // verify if token has been saved correctly
                if (loginDao.getByToken(login.getToken()) != null) {
                    log.info("Login event saved in the database");
                } else {
                    log.error("Login event not saved");
                    throw new ValidationException("Login event not saved correctly");
                }

                return token;
            } else {
                log.warn("Invalid password");
                throw new ValidationException("Invalid password");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
    
    /**
     * Validates the credentials provided by the employee
     *
     * @param loginDto The login credentials provided by the employee
     */
    private static void validateCredentials(LoginDto loginDto) {
        log.trace("Starting verifying credentials");
        
        // check if credentials are null
        if (loginDto == null) {
            log.warn("Invalid credentials");
            throw new ValidationException("Invalid credentials");
        }
        log.info("credentials are valid");
        
        // check if email address is null, an empty string or does not contain '@' character
        String emailAddress = loginDto.getEmail();
        if (emailAddress == null || emailAddress.isBlank() || !emailAddress.contains("@")) {
            log.warn("Invalid email address");
            throw new ValidationException("Invalid email address");
        }
        log.info("email address syntax is valid");
        
        // check if password is null or an empty string
        String password = loginDto.getPassword();
        if (password == null || password.isEmpty() || password.isBlank()) {
            log.warn("Invalid password");
            throw new ValidationException("Invalid password");
        }
        log.info("password syntax is valid");
        
        log.trace("Credentials verified successfully");
    }

    /**
     * Validates the provided token by checking its existence and expiration status.
     *
     * @param token the token to be validated
     * @throws ValidationException if the token is invalid or expired
     */
    public static void checkTokenValidation(String token) {
        LoginDao loginDao = new LoginDao();
        Login login = loginDao.getByToken(token);
        
        if (login != null) {
            verifyIfExpired(login);
        } else {
            throw new ValidationException("Invalid token");
        }
    }
    
    public static void verifyIfExpired(Login login) {
        if (LocalDateTime.now().isAfter(login.getVersion().plusMinutes(20))) {
            log.warn("Expired token");
            throw new ValidationException("Expired token. Login event not found");
        }

        log.info("Valid token");
    }
    
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
}
