package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.*;
import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.SmartCard;
import net.andreabattista.InOutFlow.model.Tracking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import java.time.LocalDateTime;
import java.util.List;

public class SmartCardReader implements Runnable {
    
    private static final Logger log = LogManager.getLogger(SmartCardReader.class);
    
    private static volatile boolean running = true;
    private static final byte[] DEFAULT_KEY = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    
    public void stop() {
        running = false;
    }
    
    @Override
    public void run() {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                log.warn("No card terminals found");
            }
            CardTerminal terminal = terminals.getFirst();
            while (running) {
                if (terminal.isCardPresent()) {
                    try {
                        Card card = terminal.connect("*");
                        CardChannel channel = card.getBasicChannel();
                        byte[] uid = readUID(channel);
                        
                        if (uid != null) {
                            String smartCardId = byteArrayToHex(uid);
                            log.info("UID: " + smartCardId);
                            
                            try {
                                BaseDao.initFactory("DefaultPersistenceUnit");
                                SmartCardDao smartCardDao = new SmartCardDao();
                                SmartCard smartCard = smartCardDao.getByUUID(smartCardId);
                                
                                if (smartCard != null) {
                                    EmployeeDao employeeDao = new EmployeeDao();
                                    Employee employee = employeeDao.getBySmartCard(smartCard);
                                    log.info(employee.getEmailAddress());
                                    
                                    if (employee != null) {
                                        CompanyDao companyDao = new CompanyDao();
                                        Company company = companyDao.getByEmployee(employee);
                                        
                                        TrackingDao trackingDao = new TrackingDao();
                                        Long count = trackingDao.countLastMinute(employee);
                                        
                                        if (count == 0) {
                                            Tracking tracking = new Tracking();
                                            tracking.setEmployee(employee);
                                            tracking.setDate(LocalDateTime.now());
                                            
                                            Long countDailyTracking = trackingDao.countByEmployeeAndDate(employee, LocalDateTime.now());
                                            if ((countDailyTracking + 1L) % 2 == 0) {
                                                tracking.setNfcReader(company.getNfcReaders().get(1));
                                            } else {
                                                tracking.setNfcReader(company.getNfcReaders().get(0));
                                            }
                                            
                                            trackingDao.save(tracking);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                throw new ValidationException(e.getMessage());
                            }
                        }
                        card.disconnect(false);
                    } catch (CardException e) {
                        log.error("{}", e.getMessage());
                    }
                }
                Thread.sleep(1000); // Attendi 1 secondo prima di controllare di nuovo
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }
    
    private static byte[] readUID(CardChannel channel) throws CardException {
        // Select the card
        byte[] command = new byte[] {
            (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };
        ResponseAPDU response = channel.transmit(new CommandAPDU(command));
        if (response.getSW() == 0x9000) {
            return response.getData();
        } else {
            return null;
        }
    }
    
    private static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
