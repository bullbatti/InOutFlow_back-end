package net.andreabattista.InOutFlow.model;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = "Message.getByEmployeeAndCompleted", query = "select m " +
                "from net.andreabattista.InOutFlow.model.Message m " +
                "where m.receiver = :employee and m.isCompleted = false"),

        @NamedQuery(name = "Message.getBySenderAndDescription", query = "select m " +
                "from net.andreabattista.InOutFlow.model.Message m " +
                "where m.sender.emailAddress = :senderMail " +
                "and m.description = :description"),
})

@Entity(name = "messages")
public class Message {

    public enum MessageType {
        INTERNAL_SUPPORT,
        ADMINISTRATOR_SUPPORT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String description;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "employee_sender_id", nullable = false)
    private Employee sender;

    @ManyToOne
    @JoinColumn(name = "employe_receiver_id", nullable = false)
    private Employee receiver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Employee getSender() {
        return sender;
    }

    public void setSender(Employee sender) {
        this.sender = sender;
    }

    public Employee getReceiver() {
        return receiver;
    }

    public void setReceiver(Employee receiver) {
        this.receiver = receiver;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
