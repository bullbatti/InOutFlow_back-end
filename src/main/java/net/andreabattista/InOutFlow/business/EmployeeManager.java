package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.model.Employee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles operations related to user employee.
 *
 * @author bullbatti
 */
public class EmployeeManager {
    
    private static final Logger log = LogManager.getLogger(EmployeeManager.class);
    
    /**
     * Hashed the given password using the bcrypt hashing algorithm.
     *
     * @param password The plaintext password to be hashed
     * @return A hashed representation of the input password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
