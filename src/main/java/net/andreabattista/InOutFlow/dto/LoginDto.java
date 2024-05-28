package net.andreabattista.InOutFlow.dto;

/**
 * Represents a Data Transfer Object for storing information about an employee's login.
 *
 * @author bullbatti
 */
public class LoginDto {
    private String email;
    private String password;
    
    /**
    * Default constructor for the LoginDto class.
    */
    public LoginDto() {}
    
    /**
     * Constructs a LoginDto object with the specified email and password.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     */
    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    /**
     * Gets the email address associated with the login credentials.
     * To change its value should be used {@link #setEmail(String)}.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email address for the login credentials.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the password associated with the login credentials.
     * To change its value should be used {@link #setPassword(String)}.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password for the login credentials.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
