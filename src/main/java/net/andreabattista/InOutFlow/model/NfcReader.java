package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an NFC reader in the system.
 *
 * @author bullbatti
 */
@Entity (name = "nfc_readers")
public class NfcReader {
    
    public enum Type {
        ENTRY, EXIT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String position;
    
    @Column(name = "nfcreader_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NfcReader.Type nfcReaderType;
    
    /**
     * Gets the ID of the NFC reader. To change its value should be used {@link #setId(Long)}.
     *
     * @return The ID of the NFC reader.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the ID of the NFC reader.
     *
     * @param id The new ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the position of the NFC reader. To change its value should be used {@link #setPosition(String)}.
     *
     * @return The position of the NFC reader.
     */
    public String getPosition() {
        return position;
    }
    
    /**
     * Sets the position of the NFC reader.
     *
     * @param position The new position to set.
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    public Type getNfcReaderType() {
        return nfcReaderType;
    }
    
    public void setNfcReaderType(Type nfcReaderType) {
        this.nfcReaderType = nfcReaderType;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * The equality comparison is based solely on the ID of the NFC Reader.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NfcReader nfcReader = (NfcReader) o;
        return Objects.equals(id, nfcReader.id);
    }
    
    /**
     * Returns a hash code value for the NFC Reader.
     * The hash code is computed based on the ID of the NFC Reader.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
