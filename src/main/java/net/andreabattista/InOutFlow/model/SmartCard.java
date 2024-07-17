package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries( {
    @NamedQuery(name = "SmartCard.getByUUID", query = "select s " +
        "from net.andreabattista.InOutFlow.model.SmartCard s " +
        "where s.universalId = :uuid"),
    
    @NamedQuery(name = "SmartCard.countByUUID", query = "select count(s) " +
        "from net.andreabattista.InOutFlow.model.SmartCard s " +
        "where s.universalId = :uuid"),
    
    @NamedQuery(name = "SmartCard.getByEmployeeEmailAddress", query = "select s " +
        "from net.andreabattista.InOutFlow.model.SmartCard s " +
        "join net.andreabattista.InOutFlow.model.Employee e " +
        "on e.smartCard = s " +
        "where e.emailAddress = :emailAddress"),
})

/**
 * Represents a smart card in the system.
 *
 * @author bullbatti
 */
@Entity(name = "smart_card")
public class SmartCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "universal_id", nullable = false, unique = true)
    private String universalId;
    
    /**
     * Sets the ID of the smart card.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Sets the universal ID of the smart card.
     *
     * @param universalId The new universal ID to set.
     */
    public void setUniversalId(String universalId) {
        this.universalId = universalId;
    }
    
    /**
     * Gets the ID of the smart card. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the smart card.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Gets the universal ID of the smart card. To change its value should be used {@link #setUniversalId(String)}.
     *
     * @return The universal ID of the smart card.
     */
    public String getUniversalId() {
        return universalId;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the SmartCard.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartCard smartCard = (SmartCard) o;
        return Objects.equals(id, smartCard.id);
    }
    
    /**
     * Returns a hash code value for the SmartCard.
     * The hash code is computed based on the ID of the SmartCard.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
