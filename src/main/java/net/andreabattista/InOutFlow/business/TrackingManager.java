package net.andreabattista.InOutFlow.business;

import net.andreabattista.InOutFlow.dao.CompanyDao;
import net.andreabattista.InOutFlow.dao.EmployeeDao;
import net.andreabattista.InOutFlow.dao.TrackingDao;
import net.andreabattista.InOutFlow.dto.DeleteAndCreateTrackingDto;
import net.andreabattista.InOutFlow.dto.ModifyTrackingEmployeeDto;
import net.andreabattista.InOutFlow.dto.TrackingDto;
import net.andreabattista.InOutFlow.model.Company;
import net.andreabattista.InOutFlow.model.Employee;
import net.andreabattista.InOutFlow.model.NfcReader;
import net.andreabattista.InOutFlow.model.Tracking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TrackingManager {

    private static final Logger log = LogManager.getLogger(TrackingManager.class);

    /**
     * Retrieves a list of tracking records for a specific employee and date.
     * Fetches the employee based on the provided login token and retrieves
     * their tracking data for the specified date. Depending on the number of tracking records
     * found, it constructs appropriate tracking DTOs (Data Transfer Objects) which are then
     * added to the returned list.
     *
     * @param token the login token used to identify the employee
     * @param date  the date for which tracking data is being requested
     * @return a list of {@link TrackingDto} objects representing the employee's tracking records for the given date
     * @throws ValidationException if the employee is not found or if an error occurs during the retrieval of tracking data
     */
    public static List<TrackingDto> getByEmployeeAndDate(String token, LocalDateTime date) {
        List<TrackingDto> trackingDtoList = new ArrayList<>();

        // prevent show future event to make the application realistic
        if (date.toLocalDate().isAfter(LocalDate.now())) {
            log.info("Fake data hidden");
            return null;
        }

        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByLoginToken(token);

        if (employee != null) {
            TrackingDao trackingDao = new TrackingDao();
            List<Tracking> tracking = trackingDao.getByEmployeeAndDate(employee, date);

            TrackingDto trackingDto = null;
            List<Tracking> entries = null;
            List<Tracking> eventsToConnect = null;

            switch (tracking.size()) {
                case 0:
                    log.info("No tracking found");
                    break;
                case 1:
                    trackingDto = createTrackingDtoWithOnlyEntry(tracking.getFirst());
                    trackingDtoList.add(trackingDto);
                    break;
                case 2:
                    trackingDto = createEventWithEntryAndExit(tracking);
                    trackingDtoList.add(trackingDto);
                    break;
                case 3:
                    entries = new ArrayList<>();
                    eventsToConnect = new ArrayList<>();

                    for (Tracking t : tracking) {
                        if (t.getNfcReader().getNfcReaderType().equals(NfcReader.Type.ENTRY)) {
                            entries.add(t);
                        } else {
                            eventsToConnect.add(t);
                        }
                    }

                    if (eventsToConnect.getFirst().getDate().isBefore(entries.getLast().getDate())) {
                        eventsToConnect.add(entries.getFirst());
                        entries.remove(entries.getFirst());
                    } else {
                        eventsToConnect.add(entries.getLast());
                        entries.remove(entries.getLast());
                    }

                    trackingDto = createTrackingDtoWithOnlyEntry(entries.getFirst());
                    trackingDtoList.add(trackingDto);

                    trackingDto = createEventWithEntryAndExit(eventsToConnect);
                    trackingDtoList.add(trackingDto);
                    break;
                case 4:
                    entries = new ArrayList<>();
                    eventsToConnect = new ArrayList<>();
                    List<Tracking> exit = new ArrayList<>();

                    for (Tracking t : tracking) {
                        if (t.getNfcReader().getNfcReaderType().equals(NfcReader.Type.ENTRY)) {
                            entries.add(t);
                        } else {
                            exit.add(t);
                        }
                    }


                    if (exit.getFirst().getDate().isBefore(entries.getLast().getDate())) {
                        eventsToConnect.add(entries.getFirst());
                        eventsToConnect.add(exit.getFirst());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);

                        eventsToConnect.clear();
                        eventsToConnect.add(entries.getLast());
                        eventsToConnect.add(exit.getLast());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);
                    } else {
                        eventsToConnect.add(entries.getFirst());
                        eventsToConnect.add(exit.getLast());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);

                        eventsToConnect.clear();
                        eventsToConnect.add(entries.getLast());
                        eventsToConnect.add(exit.getFirst());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);
                    }
                    break;
                default:
                    throw new ValidationException("Errore retrieving tracking data");
            }
        } else {
            throw new ValidationException("Employee not found");
        }

        return trackingDtoList;
    }

    /**
     * Retrieves a list of tracking records for a specific employee and date.
     * Fetches the employee based on the provided login token and retrieves
     * their tracking data for the specified date. Depending on the number of tracking records
     * found, it constructs appropriate tracking DTOs (Data Transfer Objects) which are then
     * added to the returned list.
     *
     * @param date the date for which tracking data is being requested
     * @return a list of {@link TrackingDto} objects representing the employee's tracking records for the given date
     * @throws ValidationException if the employee is not found or if an error occurs during the retrieval of tracking data
     */
    public static List<TrackingDto> getByEmployeeToModifyAndDate(String email, LocalDateTime date) {
        List<TrackingDto> trackingDtoList = new ArrayList<>();

        // prevent show future event to make the application realistic
        if (date.toLocalDate().isAfter(LocalDate.now())) {
            log.info("Fake data hidden");
            return null;
        }

        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByEmailAddress(email);

        if (employee != null) {
            TrackingDao trackingDao = new TrackingDao();
            List<Tracking> tracking = trackingDao.getByEmployeeAndDate(employee, date);

            TrackingDto trackingDto = null;
            List<Tracking> entries = null;
            List<Tracking> eventsToConnect = null;

            switch (tracking.size()) {
                case 0:
                    log.info("No tracking found");
                    break;
                case 1:
                    trackingDto = createTrackingDtoWithOnlyEntry(tracking.getFirst());
                    trackingDtoList.add(trackingDto);
                    break;
                case 2:
                    trackingDto = createEventWithEntryAndExit(tracking);
                    trackingDtoList.add(trackingDto);
                    break;
                case 3:
                    entries = new ArrayList<>();
                    eventsToConnect = new ArrayList<>();

                    for (Tracking t : tracking) {
                        if (t.getNfcReader().getNfcReaderType().equals(NfcReader.Type.ENTRY)) {
                            entries.add(t);
                        } else {
                            eventsToConnect.add(t);
                        }
                    }

                    if (eventsToConnect.getFirst().getDate().isBefore(entries.getLast().getDate())) {
                        eventsToConnect.add(entries.getFirst());
                        entries.remove(entries.getFirst());
                    } else {
                        eventsToConnect.add(entries.getLast());
                        entries.remove(entries.getLast());
                    }

                    trackingDto = createTrackingDtoWithOnlyEntry(entries.getFirst());
                    trackingDtoList.add(trackingDto);

                    trackingDto = createEventWithEntryAndExit(eventsToConnect);
                    trackingDtoList.add(trackingDto);
                    break;
                case 4:
                    entries = new ArrayList<>();
                    eventsToConnect = new ArrayList<>();
                    List<Tracking> exit = new ArrayList<>();

                    for (Tracking t : tracking) {
                        if (t.getNfcReader().getNfcReaderType().equals(NfcReader.Type.ENTRY)) {
                            entries.add(t);
                        } else {
                            exit.add(t);
                        }
                    }


                    if (exit.getFirst().getDate().isBefore(entries.getLast().getDate())) {
                        eventsToConnect.add(entries.getFirst());
                        eventsToConnect.add(exit.getFirst());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);

                        eventsToConnect.clear();
                        eventsToConnect.add(entries.getLast());
                        eventsToConnect.add(exit.getLast());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);
                    } else {
                        eventsToConnect.add(entries.getFirst());
                        eventsToConnect.add(exit.getLast());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);

                        eventsToConnect.clear();
                        eventsToConnect.add(entries.getLast());
                        eventsToConnect.add(exit.getFirst());

                        trackingDto = createEventWithEntryAndExit(eventsToConnect);
                        trackingDtoList.add(trackingDto);
                    }
                    break;
                default:
                    throw new ValidationException("Errore retrieving tracking data");
            }
        } else {
            throw new ValidationException("Employee not found");
        }

        return trackingDtoList;
    }

    /**
     * Creates a {@link TrackingDto} object from a single entry {@link Tracking} record.
     * Initializes a {@link TrackingDto} with the start date and NFC reader details
     * from the provided {@link Tracking} record. The end date of the DTO is set based on the
     * current time.
     *
     * @param t the {@link Tracking} record containing the entry data
     * @return a {@link TrackingDto} object with the entry information
     */
    private static TrackingDto createTrackingDtoWithOnlyEntry(Tracking t) {
        TrackingDto dto = new TrackingDto();
        NfcReader reader = t.getNfcReader();

        dto.setStartDate(t.getDate());
        dto.setNfcReader(reader.getPosition() + " " + reader.getNfcReaderType().name());

        if (LocalDateTime.now().getMinute() == dto.getStartDate().getMinute()) {
            dto.setEndDate(dto.getStartDate().plusMinutes(2L));
        } else {
            dto.setEndDate(LocalDateTime.now());
        }

        return dto;
    }

    /**
     * Creates a {@link TrackingDto} object from a list of entry and exit {@link Tracking} records.
     * Initializes a {@link TrackingDto} with the start and end dates from the provided
     * list of {@link Tracking} records. It iterates through the list to identify entry and exit records,
     * setting the start date and end date accordingly. The NFC reader details are concatenated for both
     * entry and exit records and set in the DTO.
     *
     * @param tracking a list of {@link Tracking} records containing both entry and exit data
     * @return a {@link TrackingDto} object with the entry and exit information
     */
    private static TrackingDto createEventWithEntryAndExit(List<Tracking> tracking) {
        TrackingDto dto = new TrackingDto();
        NfcReader reader = null;
        String entryNfc = null;
        String exitNfc = null;

        for (Tracking t : tracking) {
            if (t.getNfcReader().getNfcReaderType().equals(NfcReader.Type.ENTRY)) {
                dto.setStartDate(t.getDate());
                reader = t.getNfcReader();
                entryNfc = reader.getPosition() + " " + reader.getNfcReaderType().name();
            } else {
                dto.setEndDate(t.getDate());
                reader = t.getNfcReader();
                exitNfc = reader.getPosition() + " " + reader.getNfcReaderType().name();
            }
        }

        dto.setNfcReader(entryNfc + " " + exitNfc);

        return dto;
    }

    public static int[] getLastWeekData(String token) {
        int attendance = 0;
        int absences = 0;
        int delays = 0;
        int earlyExit = 0;

        LocalDateTime now = LocalDateTime.now();

        LoginManager.checkTokenValidation(token);

        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByLoginToken(token);

        if (employee != null) {
            TrackingDao trackingDao = new TrackingDao();

            List<Tracking> dailyTracking = new ArrayList<>();
            for (int i = 0; i < 7; ++i) {
                dailyTracking = trackingDao.getByEmployeeAndDate(employee, now.minusDays(i));

                if (!dailyTracking.isEmpty()) {
                    Tracking firstOfDay = dailyTracking.getFirst();
                    for (Tracking t : dailyTracking) {
                        if (t.getDate().isBefore(firstOfDay.getDate())) {
                            firstOfDay = t;
                        }
                    }

                    Tracking lastOfDay = firstOfDay;
                    for (Tracking t : dailyTracking) {
                        if (t.getDate().isAfter(firstOfDay.getDate())) {
                            lastOfDay = t;
                        }
                    }

                    // data management
                    ++attendance;

                    if (((lastOfDay.getDate().getHour() - firstOfDay.getDate().getHour()) - 1) < 9) {
                        if (firstOfDay.getDate().isAfter(LocalDateTime.of(firstOfDay.getDate().toLocalDate(), LocalTime.of(9, 10, 0)))) {
                            ++delays;
                        } else {
                            ++earlyExit;
                        }
                    }


                } else if (now.minusDays(i).getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                        now.minusDays(i).getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    log.info("Public holiday, no work");

                } else {
                    ++absences;
                }
            }
        }

        return new int[]{
                attendance,
                absences,
                delays,
                earlyExit
        };
    }

    public static ArrayList<Integer[]> getCurrentYearData(String token) {
        ArrayList<Integer[]> allYearData = new ArrayList<>();
        ArrayList<Integer> attArr = new ArrayList<>();
        ArrayList<Integer> absArr = new ArrayList<>();
        ArrayList<Integer> delArr = new ArrayList<>();
        ArrayList<Integer> earlArr = new ArrayList<>();

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstOfYear = today.withDayOfYear(1);

        LoginManager.checkTokenValidation(token);

        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = employeeDao.getByLoginToken(token);

        if (employee != null) {
            TrackingDao trackingDao = new TrackingDao();

            List<Tracking> dailyTracking = new ArrayList<>();

            for (int i = 1; i <= LocalDate.now().getMonthValue(); ++i) {

                int attendance = 0;
                int absences = 0;
                int delays = 0;
                int earlyExit = 0;

                if (i > 0) {
                    firstOfYear = firstOfYear.plusMonths(1);
                }
                for (int j = 0; j < firstOfYear.getMonth().length(false); ++j) {
                    if (i == LocalDate.now().getMonthValue() && j == LocalDate.now().getDayOfMonth()) {
                        break;
                    }

                    dailyTracking = trackingDao.getByEmployeeAndDate(employee, firstOfYear.plusDays(j));
                    if (!dailyTracking.isEmpty()) {
                        Tracking firstOfDay = dailyTracking.getFirst();
                        for (Tracking t : dailyTracking) {
                            if (t.getDate().isBefore(firstOfDay.getDate())) {
                                firstOfDay = t;
                            }
                        }

                        Tracking lastOfDay = firstOfDay;
                        for (Tracking t : dailyTracking) {
                            if (t.getDate().isAfter(firstOfDay.getDate())) {
                                lastOfDay = t;
                            }
                        }

                        // data management
                        ++attendance;

                        if (((lastOfDay.getDate().getHour() - firstOfDay.getDate().getHour()) - 1) < 9) {
                            if (firstOfDay.getDate().isAfter(LocalDateTime.of(firstOfDay.getDate().toLocalDate(), LocalTime.of(9, 10, 0)))) {
                                ++delays;
                            } else {
                                ++earlyExit;
                            }
                        }


                    } else if (firstOfYear.plusDays(j).getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                            firstOfYear.plusDays(j).getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        log.info("Giorno non lavorativo. NOn calcolo nulla");

                    } else {
                        ++absences;
                    }
                }

                attArr.add(attendance);
                absArr.add(absences);
                delArr.add(delays);
                earlArr.add(earlyExit);
            }
        }
        allYearData.add(attArr.toArray(new Integer[0]));
        allYearData.add(absArr.toArray(new Integer[0]));
        allYearData.add(delArr.toArray(new Integer[0]));
        allYearData.add(earlArr.toArray(new Integer[0]));

        return allYearData;
    }

    public static boolean modifyEvent(ModifyTrackingEmployeeDto combined) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getByEmailAddress(combined.getEmployee().getEmailAddress());

            if (employee == null) {
                throw new ValidationException("Employee not found");
            }

            TrackingDao trackingDao = new TrackingDao();
            Tracking startTracking = trackingDao.getByEmployeeAndStartDate(employee, combined.getOldTracking().getStartDate());

            if (startTracking == null) {
                throw new ValidationException("Start date not found");
            }

            startTracking.setDate(combined.getNewTracking().getStartDate());
            trackingDao.update(startTracking);

            Tracking endTracking = trackingDao.getByEmployeeAndStartDate(employee, combined.getOldTracking().getEndDate());

            if (endTracking == null) {
                throw new ValidationException("End date not found");
            }

            endTracking.setDate(combined.getNewTracking().getEndDate());
            trackingDao.update(endTracking);

            return true;

        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public static boolean delete(DeleteAndCreateTrackingDto deleteAndCreateTrackingDto) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.getByEmailAddress(deleteAndCreateTrackingDto.getEmployee().getEmailAddress());

            if (employee == null) {
                throw new ValidationException("Employee not found");
            }

            TrackingDao trackingDao = new TrackingDao();
            Tracking startTracking = trackingDao.getByEmployeeAndStartDate(employee, deleteAndCreateTrackingDto.getTracking().getStartDate());

            if (startTracking == null) {
                throw new ValidationException("Start date not found");
            }

            trackingDao.remove(startTracking);

            Tracking endTracking = trackingDao.getByEmployeeAndStartDate(employee, deleteAndCreateTrackingDto.getTracking().getEndDate());

            trackingDao.remove(endTracking);

            return true;

        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public static boolean create(DeleteAndCreateTrackingDto dto) {
        try {
            TrackingDao trackingDao = new TrackingDao();
            EmployeeDao employeeDao = new EmployeeDao();
            CompanyDao companyDao = new CompanyDao();

            Employee employee = employeeDao.getByEmailAddress(dto.getEmployee().getEmailAddress());

            if (employee == null) {
                throw new ValidationException("Employee not found");
            }

            Company comapany = companyDao.getByEmployee(employee);

            if (comapany == null) {
                throw new ValidationException("Company not found");
            }

            List<Tracking> trackings = trackingDao.getByEmployeeAndDate(employee, dto.getTracking().getStartDate().toLocalDate().atStartOfDay());

            if ((trackings.isEmpty() || trackings.size() == 2) && dto.getTracking() != null) {
                Tracking tracking = new Tracking();
                tracking.setEmployee(employee);
                tracking.setDate(dto.getTracking().getStartDate());
                tracking.setNfcReader(comapany.getNfcReaders().getFirst());
                trackingDao.save(tracking);

                tracking = new Tracking();
                tracking.setEmployee(employee);
                tracking.setDate(dto.getTracking().getEndDate());
                tracking.setNfcReader(comapany.getNfcReaders().getLast());
                trackingDao.save(tracking);
            }

            return true;
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}



