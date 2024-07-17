package net.andreabattista.InOutFlow.dto;

/**
 * Data Transfer Object (DTO) for inserting a new Employee.
 * Represents the data required to create a new employee.
 */
public class EmployeeToInsertDto {
    
    private String firstName;
    private String lastName;
    private String birthDate;
    private String phoneNumber;
    private String emailAddress;
    private String accountType;
    private String smartCardNumber;


    /**
     * Gets the first name of the entity.
     *
     * @return the first name as a String.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the entity.
     *
     * @param firstName the new first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the entity.
     *
     * @return the last name as a String.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the entity.
     *
     * @param lastName the new last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the birth date of the entity.
     *
     * @return the birth date as a String.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the entity.
     *
     * @param birthDate the new birth date to set.
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the phone number of the entity.
     *
     * @return the phone number as a String.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the entity.
     *
     * @param phoneNumber the new phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the entity.
     *
     * @return the email address as a String.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the entity.
     *
     * @param emailAddress the new email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the account type of the entity.
     *
     * @return the account type as a String.
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the account type of the entity.
     *
     * @param accountType the new account type to set.
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Gets the smart card number of the entity.
     *
     * @return the smart card number as a String.
     */
    public String getSmartCardNumber() {
        return smartCardNumber;
    }

    /**
     * Sets the smart card number of the entity.
     *
     * @param smartCardNumber the new smart card number to set.
     */
    public void setSmartCardNumber(String smartCardNumber) {
        this.smartCardNumber = smartCardNumber;
    }
}
