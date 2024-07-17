package net.andreabattista.InOutFlow.dto;

import net.andreabattista.InOutFlow.model.AccountType;

/**
 * Data Transfer Object (DTO) for Employee.
 * Represents the data of an employee that is transferred between processes.
 */
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String rollNumber;
    private AccountType accountType;
    private String emailAddress;
    private boolean changePassword;
    private String image;
    
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
     * Gets the type of account that an employee has. To change its value should be used {@link #setAccountType(AccountType)}.
     *
     * @return The type of account that an employee has.
     */
    public AccountType getAccountType() {
        return accountType;
    }

    /**
     * Sets the type of account that an employee has.
     *
     * @param accountType The type of account.
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /**
     * Gets the email address associated with the entity.
     *
     * @return the email address as a String.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address associated with the entity.
     *
     * @param emailAddress the new email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Checks if the password change is required.
     *
     * @return true if the password change is required, false otherwise.
     */
    public boolean isChangePassword() {
        return changePassword;
    }

    /**
     * Gets the image associated with the entity.
     *
     * @return the image as a String.
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image associated with the entity.
     *
     * @param image the new image to set.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Sets the password change requirement status.
     *
     * @param changePassword the new password change requirement status to set.
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }
}
