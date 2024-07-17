package net.andreabattista.InOutFlow.dto;

import net.andreabattista.InOutFlow.model.Message;

public class MessageSentDto {
    private String emailAddress;
    private String description;
    private Message.MessageType type;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Message.MessageType getType() {
        return type;
    }

    public void setType(Message.MessageType type) {
        this.type = type;
    }
}
