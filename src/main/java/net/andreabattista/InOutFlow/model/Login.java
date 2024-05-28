package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 * Represents a login in the system.
 *
 * @author bullbatti
 */
@NamedQueries({
    @NamedQuery(name = "Login.getByToken", query = "select l " +
        "from net.andreabattista.InOutFlow.model.Login l " +
        "where l.token = :token "),
})

@Entity
public class Login {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @Column(name = "generation_time", nullable = false)
    private LocalDateTime generationTime;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    /**
     * Gets the ID of the login entity. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the login entity.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the login entity.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the token associated with the login. To change its value should be used {@link #setToken(String)}.
     *
     * @return The token associated with the login.
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Sets the token associated with the login.
     *
     * @param token The new token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Gets the generation time of the token. To change its value should be used {@link #setGenerationTime(LocalDateTime).
     *
     * @return The token associated with the login.
     */
    public LocalDateTime getGenerationTime() {
        return generationTime;
    }
    
    /**
     * Sets the generation time of the token
     *
     * @param generationTime The generation time of the token
     */
    public void setGenerationTime(LocalDateTime generationTime) {
        this.generationTime = generationTime;
    }
    
    /**
     * Gets the employee associated with the login. To change its value should be used {@link #setEmployee(Employee)}.
     *
     * @return The employee associated with the login.
     */
    public Employee getEmployee() {
        return employee;
    }
    
    /**
     * Sets the employee associated with the login.
     *
     * @param employee The new employee to associate with the login.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the Login.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(id, login.id);
    }
    
    /**
     * Returns a hash code value for the Login.
     * The hash code is computed based on the ID of the Login.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
