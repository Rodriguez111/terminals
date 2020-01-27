package terminals.controller.logic;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.Registration;
import terminals.storage.DBRegistration;
import terminals.storage.DBTerminal;
import terminals.storage.RegistrationStorage;
import terminals.storage.TerminalStorage;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateRegs implements RegValidator {
//    private static final Logger LOG = LoggerFactory.getLogger(ValidateRegs.class);
    private final static ValidateRegs INSTANCE = new ValidateRegs();
    private final static TerminalRegistrationValidator TERMINAL_REGISTRATION_VALIDATOR = ValidateTerminalRegistration.getINSTANCE();
    private final static UserRegistrationValidator USER_REGISTRATION_VALIDATOR = ValidateUserRegistration.getINSTANCE();
    private RegistrationStorage regStorage = DBRegistration.getINSTANCE();
    private TerminalStorage terminalStorage = DBTerminal.getINSTANCE();
    private ValidateRegs() {
    }

    public static ValidateRegs getINSTANCE() {
        return INSTANCE;
    }

    public void setRegStorage(RegistrationStorage regStorage) {
        this.regStorage = regStorage;
    }

    public void setTerminalStorage(TerminalStorage terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    @Override
    public List<Registration> findAllEntriesForTheLastDay() {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String yesterday = formatter.format(now);
        List<Registration> list = regStorage.findAllEntriesForTheLastDay(yesterday);
        list.sort(new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return list;
    }

    @Override
    public List<Registration> findEntriesByFilter(HttpServletRequest request) {
//        LOG.info("Enter method");
        Map<String, String> mapOfFilters = generateParameters(request);

        List<Registration> list = regStorage.findEntriesByFilter(mapOfFilters);
        list.sort(new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
//        LOG.info("Exit method");
        return list;
    }

    public JSONObject validateTerminalInput(String terminalSerialId) {
        return TERMINAL_REGISTRATION_VALIDATOR.validateTerminalInput(terminalSerialId);
    }

    @Override
    public JSONObject validateUserInputForGiving(String stringFromClient) {
//        LOG.info("Enter and exit method");

        return USER_REGISTRATION_VALIDATOR.validateUserInputForGiving(stringFromClient);
    }

    @Override
    public JSONObject validateUserInputForReceiving(String stringFromClient) {
//        LOG.info("Enter and exit method");
        return USER_REGISTRATION_VALIDATOR.validateUserInputForReceiving(stringFromClient);
    }

    private Map<String, String> generateParameters(HttpServletRequest request) {
//       LOG.info("Enter method");
       Map<String, String> map = new HashMap<>();
       String regIdFilter = request.getParameter("regIdFilter");
       String loginFilter = request.getParameter("loginFilter");
       String fullNameFilter = request.getParameter("fullNameFilter");
       String whoGaveFilter = request.getParameter("whoGaveFilter");
        String whoReceivedFilter = request.getParameter("whoReceivedFilter");
       String startDateFilterFrom = request.getParameter("startDateFilterFrom");
       String startDateFilterTo = request.getParameter("startDateFilterTo");
       String endDateFilterFrom = request.getParameter("endDateFilterFrom");
       String endDateFilterTo = request.getParameter("endDateFilterTo");
       if(!regIdFilter.equals("")) {
           map.put("regIdFilter", regIdFilter);
       }
       if(!loginFilter.equals("")) {
           map.put("loginFilter", loginFilter);
       }
       if(!fullNameFilter.equals("")) {
           map.put("fullNameFilter", fullNameFilter);
       }
       if(!whoGaveFilter.equals("")) {
           map.put("whoGaveFilter", whoGaveFilter);
       }
        if(!whoReceivedFilter.equals("")) {
            map.put("whoReceivedFilter", whoReceivedFilter);
        }
       if(!startDateFilterFrom.equals("")) {
           map.put("startDateFilterFrom", startDateFilterFrom);
       }
       if(!startDateFilterTo.equals("")) {
           map.put("startDateFilterTo", startDateFilterTo);
       }
       if(!endDateFilterFrom.equals("")) {
           map.put("endDateFilterFrom", endDateFilterFrom);
       }
       if(!endDateFilterTo.equals("")) {
           map.put("endDateFilterTo", endDateFilterTo);
       }
//       LOG.info("Exit method");
      return map;
   }

    @Override
    public Map<List<Registration>, Map<String, String>>  sortEntries(HttpServletRequest request) {
        Map<List<Registration>, Map<String, String>> result = new HashMap<>();
        Map<String, String> mapOfButtonConditions =  filterButtonsCondition();
        String sortField = request.getParameter("sortWhat");
        String sortDirection = request.getParameter("sortDirection");
        List<Registration> entryList = (List<Registration>)request.getSession().getAttribute("listOfRegs");

        Map<String, Comparator<Registration>> map = new HashMap<>();
        map.put("sortByRegId", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByRegId", "up");
                    Integer first = 0;
                    int second = 0;
                    try{
                        first = Integer.parseInt(o1.getTerminalRegistrationId().substring(2));
                        second = Integer.parseInt(o2.getTerminalRegistrationId().substring(2));
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный формат учетного номера терминала");
                        e.printStackTrace();
                    }
                    result =  first.compareTo(second);
                } else {
                    mapOfButtonConditions.put("sortByRegId", "down");
                    Integer first = 0;
                    Integer second = 0;
                    try{
                        first = Integer.parseInt(o1.getTerminalRegistrationId().substring(2));
                        second = Integer.parseInt(o2.getTerminalRegistrationId().substring(2));
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный формат учетного номера терминала");
                        e.printStackTrace();
                    }
                    result = second.compareTo(first);
                }
                return result;
            }

        });
        map.put("sortByLogin", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByLogin", "up");
                    result = o1.getUserLogin().compareTo(o2.getUserLogin());
                } else {
                    mapOfButtonConditions.put("sortByLogin", "down");
                    result = o2.getUserLogin().compareTo(o1.getUserLogin());
                }
                return result;
            }
        });
        map.put("sortByFullName", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByFullName", "up");
                    result = o1.getUserFullName().compareTo(o2.getUserFullName());
                } else {
                    mapOfButtonConditions.put("sortByFullName", "down");
                    result = o2.getUserFullName().compareTo(o1.getUserFullName());
                }
                return result;
            }
        });
        map.put("sortByWhoGave", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByWhoGave", "up");
                    result = o1.getWhoGave().compareTo(o2.getWhoGave());
                } else {
                    mapOfButtonConditions.put("sortByWhoGave", "down");
                    result = o2.getWhoGave().compareTo(o1.getWhoGave());
                }
                return result;
            }
        });
        map.put("sortByWhoReceived", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByWhoReceived", "up");
                    result = o1.getWhoReceived().compareTo(o2.getWhoReceived());
                } else {
                    mapOfButtonConditions.put("sortByWhoReceived", "down");
                    result = o2.getWhoReceived().compareTo(o1.getWhoReceived());
                }
                return result;
            }
        });
        map.put("sortByStartDate", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByStartDate", "up");
                    result = o1.getStartDate().compareTo(o2.getStartDate());
                } else {
                    mapOfButtonConditions.put("sortByStartDate", "down");
                    result = o2.getStartDate().compareTo(o1.getStartDate());
                }
                return result;
            }
        });
        map.put("sortByEndDate", new Comparator<Registration>() {
            @Override
            public int compare(Registration o1, Registration o2) {
                int result = 0;
                if(sortDirection.equals("up")) {
                    mapOfButtonConditions.put("sortByEndDate", "up");
                    result = o1.getEndDate().compareTo(o2.getEndDate());
                } else {
                    mapOfButtonConditions.put("sortByEndDate", "down");
                    result = o2.getEndDate().compareTo(o1.getEndDate());
                }
                return result;
            }
        });

        entryList.sort(map.get(sortField));

        result.put(entryList, mapOfButtonConditions);
      return result;
    }

    @Override
   public Map<String, String> filterButtonsCondition () {
        Map<String, String> filterButtonsCondition = new HashMap<>();
        filterButtonsCondition.put("sortByRegId", "neutral");
        filterButtonsCondition.put("sortByLogin", "neutral");
        filterButtonsCondition.put("sortByFullName", "neutral");
        filterButtonsCondition.put("sortByWhoGave", "neutral");
        filterButtonsCondition.put("sortByWhoReceived", "neutral");
        filterButtonsCondition.put("sortByStartDate", "neutral");
        filterButtonsCondition.put("sortByEndDate", "neutral");
        return filterButtonsCondition;
    }

    @Override
    public int countOfTerminals(String whatToCount) {
       return terminalStorage.countOfTerminals(whatToCount);
    }
}
