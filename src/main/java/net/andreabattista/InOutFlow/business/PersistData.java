package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.*;
import net.andreabattista.InOutFlow.model.*;
import net.datafaker.Faker;

import java.time.*;
import java.util.*;

public class PersistData {
    
    public static void persist() {
        BaseDao.initFactory("DefaultPersistenceUnit");

        SmartCardDao smartCardDao = new SmartCardDao();
        NfcReaderDao nfcReaderDao = new NfcReaderDao();
        EmployeeDao employeeDao = new EmployeeDao();
        CompanyDao companyDao = new CompanyDao();
        TrackingDao trackingDao = new TrackingDao();
//        MessageDao messageDao = new MessageDao();

        List<String> allPasswords = new ArrayList<>();

        SmartCard smartCard1 = new SmartCard();
        smartCard1.setUniversalId("36611102");

        SmartCard smartCard2 = new SmartCard();
        smartCard2.setUniversalId("86610C02");

        SmartCard smartCard3 = new SmartCard();
        smartCard3.setUniversalId("86961302");

        SmartCard smartCard4 = new SmartCard();
        smartCard4.setUniversalId("E6310B02");

        SmartCard smartCard5 = new SmartCard();
        smartCard5.setUniversalId("267A1302");

        SmartCard smartCard6 = new SmartCard();
        smartCard6.setUniversalId("F69B0202");

        smartCardDao.save(smartCard1);
        smartCardDao.save(smartCard2);
        smartCardDao.save(smartCard3);
        smartCardDao.save(smartCard4);
        smartCardDao.save(smartCard5);
        smartCardDao.save(smartCard6);

        Faker faker = new Faker(Locale.ITALIAN);

        List<Message> messages = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {

            // Generates nfc readers
            List<NfcReader> nfcReaderList = new ArrayList<>();

            for(int j = 0; j < 2; ++j) {
                NfcReader nfcReader = new NfcReader();
                nfcReader.setPosition(faker.name().title());

                if (j == 0)
                    nfcReader.setNfcReaderType(NfcReader.Type.ENTRY);
                else
                    nfcReader.setNfcReaderType(NfcReader.Type.EXIT);

                nfcReaderDao.save(nfcReader);

                nfcReaderList.add(nfcReader);
            }

            // generate Employees
            List<Employee> companyEmployees = new ArrayList<>();

            for (int j = 0; j < 2; ++j) {
                Employee employee = new Employee();
                employee.setFirstName(faker.name().firstName());
                employee.setLastName(faker.name().lastName());
                employee.setBirthdate(faker.date().birthday().toLocalDateTime().toLocalDate());
                employee.setPhoneNumber(faker.phoneNumber().phoneNumber());
                employee.setEmailAddress(faker.internet().emailAddress());
                employee.setRollNumber(String.valueOf(faker.number().numberBetween(11111, 99999)));
                employee.setPasswordChanged(false);
                employee.setImage(faker.internet().image());
                String password = faker.internet().password();
                allPasswords.add(password);
                String hashedPassword = EmployeeManager.hashPassword(password);
                employee.setPassword(hashedPassword);

                if (i == 0) {
                    employee.setAccountType(AccountType.ADMINISTRATOR);

                    if (j == 0)
                        employee.setSmartCard(smartCard1);
                    else
                        employee.setSmartCard(smartCard2);
                } else {

                    if (j == 0) {
                        employee.setAccountType(AccountType.SUPPORT);
                        employee.setSmartCard(smartCard3);
                    } else {
                        employee.setAccountType(AccountType.USER);
                        employee.setSmartCard(smartCard4);
                    }
                }

                employeeDao.save(employee);

                for (long l = 0; l < 365; ++l) {
                    LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 9, 0, 0);

                        boolean random = faker.random().nextBoolean();
                        if (random) {
                            startDate = startDate.plusHours(faker.number().numberBetween(-1L, 2L));
                        }

                        random = faker.random().nextBoolean();
                        if (random) {
                            startDate = startDate.plusMinutes(faker.number().numberBetween(0L, 59L));
                        }

                        random = faker.random().nextBoolean();
                        if (random) {
                            startDate = startDate.plusSeconds(faker.number().numberBetween(0L, 59L));
                        }

                        Tracking tracking1 = new Tracking();
                        tracking1.setEmployee(employee);
                        tracking1.setDate(startDate.plusDays(l));
                        tracking1.setNfcReader(nfcReaderList.get(0));

                        startDate = startDate.plusHours(4L);

                        Tracking tracking2 = new Tracking();
                        tracking2.setEmployee(employee);
                        tracking2.setDate(startDate.plusDays(l));
                        tracking2.setNfcReader(nfcReaderList.get(1));

                        startDate = startDate.plusHours(1L);

                        Tracking tracking3 = new Tracking();
                        tracking3.setEmployee(employee);
                        tracking3.setDate(startDate.plusDays(l));
                        tracking3.setNfcReader(nfcReaderList.get(0));

                        startDate = startDate.plusHours(4L);

                        Tracking tracking4 = new Tracking();
                        tracking4.setEmployee(employee);
                        tracking4.setDate(startDate.plusDays(l));
                        tracking4.setNfcReader(nfcReaderList.get(1));

                    if (!tracking1.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !tracking1.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                        !tracking2.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !tracking2.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                        !tracking3.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !tracking3.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                        !tracking4.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                        !tracking4.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        trackingDao.save(tracking1);
                        trackingDao.save(tracking2);
                        trackingDao.save(tracking3);
                        trackingDao.save(tracking4);
                    }
                }

                companyEmployees.add(employee);


                for (int m = 0; m < 2; ++m) {
                    Message message = new Message();
                    message.setSender(employee);
                    message.setDescription(faker.shakespeare().romeoAndJulietQuote());

                    int rand = faker.number().numberBetween(1, 2);

                    if (rand == 1) {
                        message.setType(Message.MessageType.INTERNAL_SUPPORT);
                    } else {
                        message.setType(Message.MessageType.ADMINISTRATOR_SUPPORT);
                    }

                    messages.add(message);
                }
            }

            // Generates companies
            Company company = new Company();

            if (i == 0) {
                company.setBusinessName("InOutFlow");
                company.setOfficeAddress("Via di Corticella, 18, Bologna (BO)");
                company.setPhoneNumber("3406065105");
                company.setEmailAddress("battistaandreaa@gmail.com");
            } else {
                company.setBusinessName(faker.company().name());
                company.setOfficeAddress(faker.address().fullAddress());
                company.setPhoneNumber(faker.phoneNumber().phoneNumber());
                company.setEmailAddress(faker.internet().emailAddress());
            }

            company.setNfcReaders(nfcReaderList);
            company.setEmployees(companyEmployees);
            companyDao.save(company);
            
//            for (int k = 0; k < faker.number().numberBetween(0, 100L); ++k)
        }

        MessageDao messageDao = new MessageDao();
        for (Message m : messages) {
            if (m.getType().equals(Message.MessageType.INTERNAL_SUPPORT)) {
                Company company = companyDao.getByEmployee(m.getSender());
                List<Employee> all = employeeDao.getByCompany(company.getBusinessName());
                List<Employee> support = new ArrayList<>();

                for (Employee e : all) {
                    if (!e.getAccountType().equals(AccountType.USER)) {
                        support.add(e);
                    }
                }

                if (support.size() > 1) {
                    m.setReceiver(support.get(faker.number().numberBetween(1, support.size())));
                } else {
                    m.setReceiver(support.getFirst());
                }
            } else {
                List<Employee> all = employeeDao.getAll(Employee.class);
                List<Employee> admins = new ArrayList<>();

                for (Employee e : all) {
                    if (e.getAccountType().equals(AccountType.ADMINISTRATOR)) {
                        admins.add(e);
                    }
                }

                m.setReceiver(admins.get(faker.number().numberBetween(1, admins.size())));
            }

            messageDao.save(m);

        }

        System.out.println("\nPasswords");
        for (String p : allPasswords) {
            System.out.println(p);
        }
    }
}
