package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an employee of a company present in the system.
 *
 * @author bullbatti
 */
@NamedQueries({
    @NamedQuery( name = "Employee.getByEmail", query = "select e " +
        "from net.andreabattista.InOutFlow.model.Employee e " +
        "where e.emailAddress = :emailAddress"),
    
    @NamedQuery( name = "Employee.countByEmail", query = "select count(e) " +
        "from net.andreabattista.InOutFlow.model.Employee e " +
        "where e.emailAddress = :emailAddress"),
    
    @NamedQuery(name = "Employee.getByLoginToken", query = "select e " +
        "from net.andreabattista.InOutFlow.model.Login l " +
        "join l.employee e " +
        "where l.token = :token"),
    
    @NamedQuery(name = "Employee.getBySmartCard", query = "select e " +
        "from net.andreabattista.InOutFlow.model.Employee e " +
        "where e.smartCard = :smartCard"),
    
    @NamedQuery(name = "Employee.getByCompany", query = "select e " +
        "from net.andreabattista.InOutFlow.model.Employee e " +
        "join net.andreabattista.InOutFlow.model.Company c on e " +
        "member of c.employees where c.businessName = :businessName "),
    
    @NamedQuery(name = "Employee.countByRollNumber", query = "select count(e) " +
        "from net.andreabattista.InOutFlow.model.Employee e " +
        "where e.rollNumber = :rollNumber "),

    @NamedQuery(name = "Employee.getByCompanyAndSupportType", query = "select e " +
            "from net.andreabattista.InOutFlow.model.Employee e " +
            "join net.andreabattista.InOutFlow.model.Company c on e " +
            "member of c.employees where c = :company and e.accountType != :accountType and e != :sender"),

    @NamedQuery(name = "Employee.getByAdministratorType", query = "select e " +
            "from net.andreabattista.InOutFlow.model.Employee e " +
            "where e.accountType = :accountType and e != :sender"),

    @NamedQuery(name = "Employee.getByPhoneNumber", query = "select e " +
            "from net.andreabattista.InOutFlow.model.Employee e " +
            "where e.phoneNumber = :phoneNumber"),

    @NamedQuery(name = "Employee.getByRollNumber", query = "select e " +
            "from net.andreabattista.InOutFlow.model.Employee e " +
            "where e.rollNumber = :rollNumber"),
})


@Entity
public class Employee {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private LocalDate birthdate;
    
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;
    
    @Column(name = "roll_number", nullable = false)
    private String rollNumber;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinTable(name = "employee_smart_card",
        joinColumns = @JoinColumn( name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "smartcard_id"))
    private SmartCard smartCard;

    @Column(name = "is_password_changed", nullable = false)
    private boolean isPasswordChanged;

    @Column (nullable = false)
    private String image;
    
    /**
     * Gets the ID of the employee. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the employee.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the employee.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the first name of the employee. To change its value should be used {@link #setFirstName(String)}.
     *
     * @return The first name of the employee.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name of the employee.
     *
     * @param name The new first name to set.
     */
    public void setFirstName(String name) {
        this.firstName = name;
    }
    
    /**
     * Gets the last name of the employee. To change its value should be used {@link #setLastName(String)}.
     *
     * @return The last name of the employee.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the last name of the employee.
     *
     * @param surname The new last name to set.
     */
    public void setLastName(String surname) {
        this.lastName = surname;
    }
    
    /**
     * Gets the birthdate of the employee. To change its value should be used {@link #setBirthdate(LocalDate)}.
     *
     * @return The birthdate of the employee.
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }
    
    /**
     * Sets the birthdate of the employee.
     *
     * @param birthDate The new birthdate to set.
     */
    public void setBirthdate(LocalDate birthDate) {
        this.birthdate = birthDate;
    }
    
    /**
     * Gets the phone number of the employee. To change its value should be used {@link #setPhoneNumber(String)}.
     *
     * @return The phone number of the employee.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the phone number of the employee.
     *
     * @param phoneNumber The new phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the email address of the employee. To change its value should be used {@link #setEmailAddress(String)}.
     *
     * @return The email address of the employee.
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    
    /**
     * Sets the email address of the employee.
     *
     * @param email The new email address to set.
     */
    public void setEmailAddress(String email) {
        this.emailAddress = email;
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
     * @param rollNumber The new roll number to set.
     */
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    
    /**
     * Gets the password of the employee.
     *
     * @return The password of the employee.
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password of the employee.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the account type of the employee.
     *
     * @return The account type of the employee.
     */
    public AccountType getAccountType() {
        return accountType;
    }
    
    /**
     * Sets the account type of the employee.
     *
     * @param accountType The new account type to set.
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    /**
     * Gets the smart card associated with the employee.
     *
     * @return The smart card associated with the employee.
     */
    public SmartCard getSmartCard() {
        return smartCard;
    }
    
    /**
     * Sets the smart card associated with the employee.
     *
     * @param smartCard The new smart card to set.
     */
    public void setSmartCard(SmartCard smartCard) {
        this.smartCard = smartCard;
    }

    /**
     * Checks if the password has been changed.
     *
     * @return true if the password has been changed, false otherwise.
     */
    public boolean isPasswordChanged() {
        return isPasswordChanged;
    }

    /**
     * Sets the password changed status.
     *
     * @param passwordChanged the new password changed status to set.
     */
    public void setPasswordChanged(boolean passwordChanged) {
        isPasswordChanged = passwordChanged;
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
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the Employee.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }
    
    /**
     * Returns a hash code value for the Employee.
     * The hash code is computed based on the ID of the Employee.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
