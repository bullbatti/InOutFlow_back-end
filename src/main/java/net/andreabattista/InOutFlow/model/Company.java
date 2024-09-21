package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NamedQueries({
    @NamedQuery( name = "Company.getByEmployee", query = "select c " +
        "from net.andreabattista.InOutFlow.model.Company c " +
        "join c.employees e " +
        "where e = :employee"),

    @NamedQuery( name = "Company.getByEmailAddress", query = "select c " +
            "from net.andreabattista.InOutFlow.model.Company c " +
            "where c.emailAddress = :emailAddress "),

    @NamedQuery( name = "Company.getByBusinessName", query = "select c " +
            "from net.andreabattista.InOutFlow.model.Company c " +
            "where c.businessName = :businessName "),

    @NamedQuery( name = "Company.getByPhoneNumber", query = "select c " +
            "from net.andreabattista.InOutFlow.model.Company c " +
            "where c.phoneNumber = :phoneNumber "),
})

/**
 * Represents a company that uses the system.
 *
 * @author bullbatti
 */
@Entity(name = "companies")
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "business_name", nullable = false, unique = true)
    private String businessName;
    
    @Column(name = "office_address", nullable = false)
    private String officeAddress;
    
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;
    
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(name = "company_employee",
        joinColumns = @JoinColumn( name = "company_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;
    
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(name = "company_nfc_readers",
        joinColumns = @JoinColumn( name = "company_id"),
        inverseJoinColumns = @JoinColumn(name = "nfc_reader_id"))
    private List<NfcReader> nfcReaders;
    
    /**
     * Gets the ID of the business entity. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the business entity.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the business entity.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the name of the company. To change its value should be used {@link #setBusinessName(String)}.
     *
     * @return The name of the company.
     */
    public String getBusinessName() {
        return businessName;
    }
    
    /**
     * Sets the name of the company.
     *
     * @param businessName The name to set.
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    /**
     * Gets the address of the company. To change its value should be used {@link #setOfficeAddress(String)}.
     *
     * @return The address of the company.
     */
    public String getOfficeAddress() {
        return officeAddress;
    }
    
    
    /**
     * Sets the address of the company.
     *
     * @param address The address to set
     */
    public void setOfficeAddress(String address) {
        this.officeAddress = address;
    }
    
    /**
     * Gets the phone number of the company. To change its value should be used {@link #setPhoneNumber(String)}.
     *
     * @return The phone number of the company.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the phone number of the business.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the email address of the business. To change its value should be used {@link #setEmailAddress(String)}.
     *
     * @return The email address of the business.
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    
    /**
     * Sets the email address of the business.
     *
     * @param email The new email address to set.
     */
    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }
    
    /**
     * Gets the list of employees associated with the business. To change its value should be used {@link #setEmployees(List)}.
     *
     * @return The list of employees associated with the business.
     */
    public List<Employee> getEmployees() {
        return employees;
    }
    
    /**
     * Sets the list of employees associated with the business.
     *
     * @param employees The new list of employees to set.
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    
    /**
     * Gets the list of NFC readers associated with the business. To change its value should be used {@link #setNfcReaders(List)}.
     *
     * @return The list of NFC readers associated with the business.
     */
    public List<NfcReader> getNfcReaders() {
        return nfcReaders;
    }
    
    /**
     * Sets the list of NFC readers associated with the business.
     *
     * @param nfcReaders The new list of NFC readers to set.
     */
    public void setNfcReaders(List<NfcReader> nfcReaders) {
        this.nfcReaders = nfcReaders;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the Company.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id);
    }
    
    /**
     * Returns a hash code value for the Company.
     * The hash code is computed based on the ID of the Company.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
