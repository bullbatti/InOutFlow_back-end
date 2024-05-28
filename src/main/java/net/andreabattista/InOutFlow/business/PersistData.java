package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.*;
import net.andreabattista.InOutFlow.model.*;
import net.bytebuddy.asm.Advice;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class PersistData {
    
    public static void main(String[] args) throws Exception {
        
        BaseDao.initFactory("DefaultPersistenceUnit");
        SmartCardDao smartCardDao = new SmartCardDao();
        NfcReaderDao nfcReaderDao = new NfcReaderDao();
        EmployeeDao employeeDao = new EmployeeDao();
        CompanyDao companyDao = new CompanyDao();
        TrackingDao trackingDao = new TrackingDao();
        
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
                String password = faker.internet().password();
                allPasswords.add(password);
                String hashedPassword = EmployeeManager.hashPassword(password);
                employee.setPassword(hashedPassword);
                
                if (i == 0) {
                    employee.setAccountType(Employee.Type.ADMINISTRATOR);
                    
                    if (j == 0)
                        employee.setSmartCard(smartCard1);
                    else
                        employee.setSmartCard(smartCard2);
                } else if (i == 1) {
                    employee.setAccountType(Employee.Type.SUPPORT);
                    
                    if (j == 0)
                        employee.setSmartCard(smartCard3);
                    else
                        employee.setSmartCard(smartCard4);
                } else {
                    employee.setAccountType(Employee.Type.USER);
                    
                    if (j == 0)
                        employee.setSmartCard(smartCard5);
                    else
                        employee.setSmartCard(smartCard6);
                }
                
                employeeDao.save(employee);
                
                for (long l = 0; l < 365; ++l) {
                    LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 9, 0, 0);
                    
                    startDate = startDate.plusMinutes(l);
                    startDate = startDate.plusHours(faker.number().numberBetween(-1L, 2L));
                    startDate = startDate.plusMinutes(faker.number().numberBetween(0L, 59L));
                    startDate = startDate.plusSeconds(faker.number().numberBetween(0L, 59L));
                    
                    Tracking tracking1 = new Tracking();
                    tracking1.setEmployee(employee);
                    tracking1.setDate(startDate.plusDays(l));
                    tracking1.setNfcReader(nfcReaderList.get(0));
                    
                    startDate = startDate.plusHours(9L);
                    
                    Tracking tracking2 = new Tracking();
                    tracking2.setEmployee(employee);
                    tracking2.setDate(startDate.plusDays(l));
                    tracking2.setNfcReader(nfcReaderList.get(1));
                    
                    trackingDao.save(tracking1);
                    trackingDao.save(tracking2);
                }
                
                companyEmployees.add(employee);
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
        }
        
        
        
        BaseDao.closeFactory();
        
        System.out.println("\nPasswords");
        for (String p : allPasswords) {
            System.out.println(p);
        }
    }
}
