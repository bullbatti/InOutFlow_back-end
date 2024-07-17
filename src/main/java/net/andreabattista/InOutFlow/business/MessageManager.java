package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.CompanyDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.MessageDao;
import net.andreabattista.InOutFlow.dto.MessageDto;
import net.andreabattista.InOutFlow.dto.MessageSentDto;
import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Message;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private static final Faker faker = new Faker();

    public static List<MessageDto> getMessagesToRead(String token) {
        EmployeeDao employeeDao = new EmployeeDao();
        MessageDao messageDao = new MessageDao();

        List<MessageDto> dto = new ArrayList<>();

        Employee employee = employeeDao.getByLoginToken(token);

        List<Message> messages = messageDao.getMessagesToRead(employee);

        if (messages == null) {
            throw new ValidationException("No message found");
        }

        for (Message m : messages) {
            MessageDto mess = new MessageDto();
            mess.setSenderMail(m.getSender().getEmailAddress());
            mess.setType(m.getType());
            mess.setDescription(m.getDescription());
            mess.setSenderImage(m.getSender().getImage());
            mess.setCompleted(false);

            dto.add(mess);
        }

        return dto;
    }

    public static boolean setMessageToCompleted(MessageDto message) {
        MessageDao messageDao = new MessageDao();
        Message mes = messageDao.getBySenderAndDescription(message.getSenderMail(), message.getDescription());

        if (mes == null) {
            throw new ValidationException("No message found");
        }

        mes.setCompleted(true);
        messageDao.update(mes);

        mes = messageDao.getBySenderAndDescription(message.getSenderMail(), message.getDescription());
        return mes != null;
    }

    public static boolean saveMessage(MessageSentDto message) {
        MessageDao messageDao = new MessageDao();
        EmployeeDao employeeDao = new EmployeeDao();
        CompanyDao companyDao = new CompanyDao();

        Employee sender = employeeDao.getByEmailAddress(message.getEmailAddress());

        if (sender == null) {
            throw new ValidationException("No sender found");
        }

        Company company = companyDao.getByEmployee(sender);

        if (company == null) {
            throw new ValidationException("No company found");
        }


        Message mes = new Message();
        mes.setSender(sender);
        mes.setType(message.getType());
        mes.setCompleted(false);
        mes.setDescription(message.getDescription());

        if (message.getType().equals(Message.MessageType.INTERNAL_SUPPORT)) {
            List<Employee> allInternalSupport = employeeDao.getAllCompanySupportEmployees(company, sender);
            if (allInternalSupport.size() > 1) {
                mes.setReceiver(allInternalSupport.get(faker.number().numberBetween(0, allInternalSupport.size())));
            } else {
                mes.setReceiver(allInternalSupport.getFirst());
            }
        } else {
            List<Employee> allAdministrators = employeeDao.getAllAdministrators(sender);
            if (allAdministrators.size() > 1) {
                mes.setReceiver(allAdministrators.get(faker.number().numberBetween(0, allAdministrators.size())));
            } else {
                mes.setReceiver(allAdministrators.getFirst());
            }
        }

        messageDao.save(mes);

        return true;
    }
}
