package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.BaseDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.TrackingDao;
import net.andreabattista.InOutFlow.dto.TrackingDto;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.Tracking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrackingManager {
    
    private static final Logger log = LogManager.getLogger(TrackingManager.class);
    public static Employee getEmployee(String token) {
        Employee employee;
        
        try {
            EmployeeDao dao = new EmployeeDao();
            employee = dao.getByLoginToken(token);
        } catch (Exception e) {
            throw new ValidationException("User not found");
        }
        
        return employee;
    }
    public static List<TrackingDto> getByEmployee(String token) {
        List<TrackingDto> dto = new ArrayList<>();

        try {
            Employee employee = getEmployee(token);
            
            TrackingDao trackingDao = new TrackingDao();
            List<Tracking> tracking = trackingDao.getByUser(employee);
            
            for (Tracking t : tracking) {
                TrackingDto trackingDto = new TrackingDto();
                trackingDto.setDate(t.getDate());
                trackingDto.setNfcReader(t.getNfcReader().getPosition() + " - " + t.getNfcReader().getNfcReaderType());
                
                dto.add(trackingDto);
            }
            
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
        
        return dto;
    }
    
    /**
     * Filters all tracking in the last seven days and divides them in four categories: attendance, absences, delays, earlyExits.
     * If the entry tracking is after 9:10am the attendance is saved with delays
     *
     * @param dto
     * @return
     */
    public static int[] getWeekData(List<TrackingDto> dto) {
        ArrayList<TrackingDto> entryTracking = new ArrayList<>();
        
        int attendance = 0;
        int absences = 0;
        int delays = 0;
        int earlyExits = 0;
        
        for (TrackingDto t : dto) {
            if (t.getDate().isBefore(LocalDate.now().atStartOfDay()) && t.getDate().isAfter(LocalDate.now().minusDays(7L).atStartOfDay())) {
                int index = t.getNfcReader().lastIndexOf(" - ");
                String subString = t.getNfcReader().substring(index);
                log.info("valore substring: {}", subString);
                
                switch (subString) {
                    
                    case " - ENTRY":
                        entryTracking.add(t);
                        
                        if (t.getDate().isBefore(LocalDateTime.of(t.getDate().toLocalDate(), LocalTime.of(9, 10, 0)))) {
                            ++attendance;
                        } else {
                            ++attendance;
                            ++delays;
                        }
                        break;
                    case " -  EXIT":
                        TrackingDto dayEntry = null;
                        for (TrackingDto e : entryTracking) {
                            if (e.getDate().toLocalDate().equals(t.getDate().toLocalDate()) && t.getDate().isBefore(e.getDate().plusHours(7L).plusMinutes(50L))) {
                                ++earlyExits;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        
        for (long i = 0; i < 7; ++i) {
            if(isDateEmpty(dto, LocalDate.now().minusDays(i))) {
                ++absences;
            }
        }

        return new int[] {attendance, absences, delays, earlyExits};
    }
    
    private static boolean isDateEmpty(List<TrackingDto> dto, LocalDate minDate) {
        for (TrackingDto t : dto) {
            if (t.getDate().toLocalDate().equals(minDate)) {
                return false;
            }
        }
        
        return true;
    }
    
//    private static void separateByMonth() {
//        ArrayList<TrackingDto> january = new ArrayList<>();
//        ArrayList<TrackingDto> february = new ArrayList<>();
//        ArrayList<TrackingDto> march = new ArrayList<>();
//        ArrayList<TrackingDto> april = new ArrayList<>();
//        ArrayList<TrackingDto> may = new ArrayList<>();
//        ArrayList<TrackingDto> june = new ArrayList<>();
//        ArrayList<TrackingDto> july = new ArrayList<>();
//        ArrayList<TrackingDto> august = new ArrayList<>();
//        ArrayList<TrackingDto> september = new ArrayList<>();
//        ArrayList<TrackingDto> october = new ArrayList<>();
//        ArrayList<TrackingDto> november = new ArrayList<>();
//        ArrayList<TrackingDto> december = new ArrayList<>();
//
//        switch()
//
//    }
    
}
