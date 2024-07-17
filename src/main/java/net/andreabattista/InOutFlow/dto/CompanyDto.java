package net.andreabattista.InOutFlow.dto;

/**
 * Data Transfer Object (DTO) for Company.
 * Represents the data of a company that is transferred between processes.
 */
public class CompanyDto {

    private String businessName;
    private String officeAddress;
    private String phoneNumber;
    private String emailAddress;

    /**
     * Gets the business name of the company.
     *
     * @return the business name of the company.
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * Sets the business name of the company.
     *
     * @param businessName the business name to set.
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Gets the office address of the company.
     *
     * @return the office address of the company.
     */
    public String getOfficeAddress() {
        return officeAddress;
    }

    /**
     * Sets the office address of the company.
     *
     * @param officeAddress the office address to set.
     */
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    /**
     * Gets the phone number of the company.
     *
     * @return the phone number of the company.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the company.
     *
     * @param phoneNumber the phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the company.
     *
     * @return the email address of the company.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the company.
     *
     * @param emailAddress the email address to set.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
