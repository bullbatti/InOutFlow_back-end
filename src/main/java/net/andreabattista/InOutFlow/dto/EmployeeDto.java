package net.andreabattista.InOutFlow.dto;

/**
 * Represents a Data Transfer Object for storing information about an employee.
 *
 * @author bullbatti
 */
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String rollNumber;
    private String accountType;
    
    /**
     * Gets the name of the employee. To change its value should be used {@link #setFirstName(String)}.
     *
     * @return The first name of the employee.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name of the employee.
     *
     * @param firstName the first name of the employee
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the surname of the employee. To change its value should be used {@link #setLastName(String)}.
     *
     * @return The first name of the employee.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the surname of the employee.
     *
     * @param lastName the surname of the employee.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the roll number of the employee. To change its value should be used {@link #setRollNumber(String)}.
     *
     * @return The roll number of the employee.
     */
    public String getRollNumber() {
        return rollNumber;
    }
    
    /**
     * Sets the roll number of the employee.
     *
     * @param rollNumber The roll number of th employee.
     */
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    /**
     * Gets the type of account that an employee has. To change its value should be used {@link #setAccountType(String)}.
     *
     * @return The type of account that an employee has.
     */
    public String getAccountType() {
        return accountType;
    }
    
    /**
     * Sets the type of account that an employee has.
     *
     * @param accountType The type of account.
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
