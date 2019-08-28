package terminals.controller.logic.fakestorage;

import terminals.models.Registration;
import terminals.models.Terminal;
import terminals.storage.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RegistrationFakeDb implements RegistrationStorage {

    private UserStorage userStorage;
    private TerminalStorage terminalStorage;

    private final static AtomicInteger COUNT = new AtomicInteger();

    private final Map<Integer, Registration> registrations = new TreeMap<>();

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void setTerminalStorage(TerminalStorage terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    @Override
    public List<Registration> findAllEntriesForTheLastDay(String yesterday) {
        return registrations.values().stream()
                .filter(registration -> yesterday.compareTo(registration.getStartDate()) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public void addEntry(Registration entry) {
        registrations.put(COUNT.incrementAndGet(), entry);
    }

    @Override
    public void updateEntry(int id, int whoReceivedId) {
        String whoReceivedLogin = userStorage.findUserById(whoReceivedId).getUserLogin();
        registrations.computeIfPresent(id, (key, value) -> {
            value.setWhoReceived(whoReceivedLogin);
            value.setEndDate(currentTimeStamp());
            return value;
        });

    }

    @Override
    public int findIdByField(Map<String, String> parameters) {
        String userLogin = userStorage.findFieldById(Integer.parseInt(parameters.get("user_id")), "user_login", null);
        String terminalRegId = terminalStorage.findFieldById(Integer.parseInt(parameters.get("terminal_id")), "terminal_reg_id", null);
        int result = -1;
        for (Map.Entry<Integer, Registration> eachEntry : registrations.entrySet()) {
            if (userLogin.equals(eachEntry.getValue().getUserLogin())
                    && terminalRegId.equals(eachEntry.getValue().getTerminalRegistrationId())
                    && eachEntry.getValue().getEndDate() == null) {

                result = eachEntry.getKey();
                break;
            }

        }
        return result;
    }

    @Override
    public List<Registration> findEntriesByFilter(Map<String, String> parameters) {
        List<Registration> result = new ArrayList<>();
        for (Registration eachEntry : new ArrayList<>(registrations.values())) {
            boolean match = false;
            if (parameters.get("regIdFilter") != null) {
                match = eachEntry.getTerminalRegistrationId().contains(parameters.get("regIdFilter"));
            }
            if (parameters.get("loginFilter") != null) {
                match = eachEntry.getUserLogin().contains(parameters.get("loginFilter"));
            }
            if (parameters.get("fullNameFilter") != null) {
                match = eachEntry.getUserFullName().contains(parameters.get("fullNameFilter"));
            }
            if (parameters.get("whoGaveFilter") != null) {
                match = eachEntry.getUserFullName().contains(parameters.get("whoGaveFilter"));
            }
            if (parameters.get("whoReceivedFilter") != null) {
                match = eachEntry.getUserFullName().contains(parameters.get("whoReceivedFilter"));
            }
            if (parameters.get("startDateFilterFrom") != null && parameters.get("startDateFilterTo") != null) {
                match = (eachEntry.getStartDate().compareTo(parameters.get("startDateFilterFrom")) >= 0
                        && eachEntry.getStartDate().compareTo(parameters.get("startDateFilterTo")) <= 0);
            }
            if (parameters.get("endDateFilterFrom") != null && parameters.get("endDateFilterTo") != null) {
                match = (eachEntry.getEndDate().compareTo(parameters.get("endDateFilterFrom")) >= 0
                        && eachEntry.getEndDate().compareTo(parameters.get("endDateFilterTo")) <= 0);
            }
            if (parameters.get("startDateFilterFrom") != null && parameters.get("startDateFilterTo") == null) {
                match = (eachEntry.getStartDate().compareTo(parameters.get("startDateFilterTo")) >= 0);
            }
            if (parameters.get("startDateFilterTo") != null && parameters.get("startDateFilterFrom") == null) {
                match = (eachEntry.getStartDate().compareTo(parameters.get("startDateFilterTo")) <= 0);
            }
            if (parameters.get("endDateFilterFrom") != null && parameters.get("endDateFilterTo") == null) {
                match = (eachEntry.getEndDate().compareTo(parameters.get("endDateFilterFrom")) >= 0);
            }
            if (parameters.get("endDateFilterTo") != null && parameters.get("endDateFilterFrom") == null) {
                match = (eachEntry.getEndDate().compareTo(parameters.get("endDateFilterTo")) <= 0);
            }

            if (match) {
                result.add(eachEntry);
            }
        }
        return result;

    }

    private String currentTimeStamp() {
        Date rawDate = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
        return dateFormat.format(rawDate);
    }


}
