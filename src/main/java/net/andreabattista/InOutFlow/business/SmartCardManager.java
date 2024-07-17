package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.BaseDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.SmartCardDao;
import net.andreabattista.InOutFlow.dto.EmployeeDto;
import net.andreabattista.InOutFlow.dto.ModifySmartCardDto;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.SmartCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.smartcardio.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SmartCardManager implements Callable<String> {
    
    private static final Logger log = LogManager.getLogger(SmartCardManager.class);
    private static volatile boolean running = true;
    
    public void stop() {
        running = false;
    }
    
    @Override
    public String call() throws Exception {
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                log.warn("No card terminals found");
                return null;
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
                                SmartCardDao smartCardDao = new SmartCardDao();
                                Long smartCardCount = smartCardDao.countByUUID(smartCardId);
                                
                                if (smartCardCount > 0) {
                                    throw new ValidationException("Smart card already used");
                                } else {
                                    return smartCardId;
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
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
        return null;
    }
    
    private static byte[] readUID(CardChannel channel) throws CardException {
        // Select the card
        byte[] command = new byte[]{
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
    
    public static String readOnlyNewSmartCard() throws Exception {
        
        SmartCardManager manager = new SmartCardManager();
        FutureTask<String> task = new FutureTask<>(manager);
        Thread thread = new Thread(task);
        thread.start();
        
        try {
            String id = task.get();
            
            if (id != null) {
                return id;
            } else {
                throw new ValidationException("Error while reading the smart card");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("Failed to read Smart Card ID");
        }
    }

    public static String getIdByEmployee(EmployeeDto employeeDto) {
        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByEmailAddress(employeeDto.getEmailAddress());

        if (employee == null) {
            throw new ValidationException("Employee not found");
        }

        return employee.getSmartCard().getUniversalId();
    }

    public static boolean modify(ModifySmartCardDto dto) {
        try {
            SmartCardDao smartCardDao = new SmartCardDao();
            SmartCard smartCard = smartCardDao.getByEmployeeEmailAddress(dto.getEmployee().getEmailAddress());

            if (smartCard == null) {
                throw new ValidationException("Smart card not found");
            }

            smartCard.setUniversalId(dto.getId());
            smartCardDao.update(smartCard);

            return true;
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
