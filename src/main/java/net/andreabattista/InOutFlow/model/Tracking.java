package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a registration event when an employee swipes the smart card on the NFC reader.
 *
 * @author bullbatti.
 */
@NamedQueries({
    @NamedQuery(name = "Tracking.getByEmployee", query = "select t " +
        "from net.andreabattista.InOutFlow.model.Tracking t " +
        "where t.employee = :employee"),
})



@Entity
public class Tracking {
    public enum Type {
        ENTRY, EXIT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "nfc_reader_id")
    private NfcReader nfcReader;
    
    /**
     * Gets the ID of the tracking entry. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the tracking entry.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the tracking entry.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the date and time of the tracking entry. To change its value should be used {@link #setDate(LocalDateTime)}.
     *
     * @return The date and time of the tracking entry.
     */
    public LocalDateTime getDate() {
        return date;
    }
    
    /**
     * Sets the date and time of the tracking entry.
     *
     * @param accessTime The new date and time to set.
     */
    public void setDate(LocalDateTime accessTime) {
        this.date = accessTime;
    }
    
    /**
     * Gets the employee associated with the tracking entry. To change its value should be used {@link #setEmployee(Employee)}.
     *
     * @return The employee associated with the tracking entry.
     */
    public Employee getEmployee() {
        return employee;
    }
    
    /**
     * Sets the employee associated with the tracking entry.
     *
     * @param employee The new employee to associate with the tracking entry.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    /**
     * Gets the NFC reader associated with the tracking entry. To change its value should be used {@link #setNfcReader(NfcReader)}.
     *
     * @return The NFC reader associated with the tracking entry.
     */
    public NfcReader getNfcReader() {
        return nfcReader;
    }
    
    /**
     * Sets the NFC reader associated with the tracking entry.
     *
     * @param nfcReader The new NFC reader to associate with the tracking entry.
     */
    public void setNfcReader(NfcReader nfcReader) {
        this.nfcReader = nfcReader;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the Tracking entry.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracking access = (Tracking) o;
        return Objects.equals(id, access.id);
    }
    
    /**
     * Returns a hash code value for the Tracking entry.
     * The hash code is computed based on the ID of the Tracking entry.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
