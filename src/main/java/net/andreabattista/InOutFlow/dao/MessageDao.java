package net.andreabattista.InOutFlow.dao;

import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Message;

import java.util.List;

public class MessageDao extends BaseDao {

    public List<Message> getMessagesToRead(Employee employee) {
        try {
            return entityManager.createNamedQuery("Message.getByEmployeeAndCompleted", Message.class)
                    .setParameter("employee", employee)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Message getBySenderAndDescription(String senderMail, String description) {
        try {
            return entityManager.createNamedQuery("Message.getBySenderAndDescription", Message.class)
                    .setParameter("senderMail", senderMail)
                    .setParameter("description", description)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
