package terminals.controller.logic;

import org.junit.Test;
import terminals.controller.logic.fakestorage.DepartmentFakeDb;
import terminals.controller.logic.fakestorage.RegistrationFakeDb;
import terminals.models.Registration;
import terminals.storage.RegistrationStorage;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ValidateRegsTest {

    @Test
    public void whenFindEntriesForTheLastDateThenReturnOnlyRecordsForTheLastDate() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        RegistrationStorage registrationStorage = new RegistrationFakeDb();
        validateRegs.setRegStorage(registrationStorage);
        Registration today = new Registration("01", "01", "01", "01",
                "1", currentTimeStamp(), null);
        Registration yesterdayPlus1Minute = new Registration("02", "02",
                "02", "01","2", yesterdayPlus1MinuteDay(), null);
        registrationStorage.addEntry(today);
        registrationStorage.addEntry(yesterdayPlus1Minute);
        List<Registration> resultList = validateRegs.findAllEntriesForTheLastDay();
        String actual = resultList.get(0).getUserLogin();
        String expected = "01";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenFindByLoginInFilterThenReturnResult() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        RegistrationStorage registrationStorage = new RegistrationFakeDb();
        validateRegs.setRegStorage(registrationStorage);
        Registration first = new Registration("011", "01", "01", "01",
                "1", currentTimeStamp(), null);
        Registration second = new Registration("02", "02",
                "02", "01","2", yesterdayPlus1MinuteDay(), null);
        registrationStorage.addEntry(first);
        registrationStorage.addEntry(second);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regIdFilter")).thenReturn("");
        when(req.getParameter("loginFilter")).thenReturn("01");
        when(req.getParameter("fullNameFilter")).thenReturn("");
        when(req.getParameter("whoGaveFilter")).thenReturn("");
        when(req.getParameter("whoReceivedFilter")).thenReturn("");
        when(req.getParameter("startDateFilterFrom")).thenReturn("");
        when(req.getParameter("startDateFilterTo")).thenReturn("");
        when(req.getParameter("endDateFilterFrom")).thenReturn("");
        when(req.getParameter("endDateFilterTo")).thenReturn("");
        List<Registration> resultList = validateRegs.findEntriesByFilter(req);
        String actual = resultList.get(0).getUserLogin();
        String expected = "011";
        assertThat(actual, is(expected));
        int actualSize = resultList.size();
        int expectedSize = 1;
        assertThat(actualSize, is(expectedSize));
    }

    @Test
    public void whenFindByLoginInFullNameThenReturnResult() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        RegistrationStorage registrationStorage = new RegistrationFakeDb();
        validateRegs.setRegStorage(registrationStorage);
        Registration first = new Registration("01", "01121", "01", "01",
                "1", currentTimeStamp(), null);
        Registration second = new Registration("02", "02",
                "02", "01","2", yesterdayPlus1MinuteDay(), null);
        registrationStorage.addEntry(first);
        registrationStorage.addEntry(second);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regIdFilter")).thenReturn("");
        when(req.getParameter("loginFilter")).thenReturn("");
        when(req.getParameter("fullNameFilter")).thenReturn("01");
        when(req.getParameter("whoGaveFilter")).thenReturn("");
        when(req.getParameter("whoReceivedFilter")).thenReturn("");
        when(req.getParameter("startDateFilterFrom")).thenReturn("");
        when(req.getParameter("startDateFilterTo")).thenReturn("");
        when(req.getParameter("endDateFilterFrom")).thenReturn("");
        when(req.getParameter("endDateFilterTo")).thenReturn("");
        List<Registration> resultList = validateRegs.findEntriesByFilter(req);
        String actual = resultList.get(0).getUserFullName();
        String expected = "01121";
        assertThat(actual, is(expected));
        int actualSize = resultList.size();
        int expectedSize = 1;
        assertThat(actualSize, is(expectedSize));
    }

    @Test
    public void whenFindByLoginAndFullNameAndFullNameDontMatchThenReturnNoResult() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        RegistrationStorage registrationStorage = new RegistrationFakeDb();
        validateRegs.setRegStorage(registrationStorage);
        Registration first = new Registration("011", "0222", "01", "01",
                "1", currentTimeStamp(), null);
        Registration second = new Registration("02", "02",
                "02", "01","2", yesterdayPlus1MinuteDay(), null);
        registrationStorage.addEntry(first);
        registrationStorage.addEntry(second);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regIdFilter")).thenReturn("");
        when(req.getParameter("loginFilter")).thenReturn("01");
        when(req.getParameter("fullNameFilter")).thenReturn("01");
        when(req.getParameter("whoGaveFilter")).thenReturn("");
        when(req.getParameter("whoReceivedFilter")).thenReturn("");
        when(req.getParameter("startDateFilterFrom")).thenReturn("");
        when(req.getParameter("startDateFilterTo")).thenReturn("");
        when(req.getParameter("endDateFilterFrom")).thenReturn("");
        when(req.getParameter("endDateFilterTo")).thenReturn("");
        List<Registration> resultList = validateRegs.findEntriesByFilter(req);
        int actualSize = resultList.size();
        int expectedSize = 0;
        assertThat(actualSize, is(expectedSize));
    }

    @Test
    public void whenFindByStartDateThenReturnResult() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        RegistrationStorage registrationStorage = new RegistrationFakeDb();
        validateRegs.setRegStorage(registrationStorage);
        Registration first = new Registration("011", "0222", "01", "01",
                "1", currentTimeStamp(), null);
        Registration second = new Registration("02", "02",
                "02", "01","2", yesterdayPlus1MinuteDay(), null);
        registrationStorage.addEntry(first);
        registrationStorage.addEntry(second);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regIdFilter")).thenReturn("");
        when(req.getParameter("loginFilter")).thenReturn("");
        when(req.getParameter("fullNameFilter")).thenReturn("");
        when(req.getParameter("whoGaveFilter")).thenReturn("");
        when(req.getParameter("whoReceivedFilter")).thenReturn("");
        when(req.getParameter("startDateFilterFrom")).thenReturn(currentTimeStampMinus1Minute());
        when(req.getParameter("startDateFilterTo")).thenReturn(currentTimeStampPlus1Minute());
        when(req.getParameter("endDateFilterFrom")).thenReturn("");
        when(req.getParameter("endDateFilterTo")).thenReturn("");
        List<Registration> resultList = validateRegs.findEntriesByFilter(req);
        String actual = resultList.get(0).getUserFullName();
        String expected = "0222";
        assertThat(actual, is(expected));
        int actualSize = resultList.size();
        int expectedSize = 1;
        assertThat(actualSize, is(expectedSize));
    }


    @Test
    public void whenSortByLoginThenReturnSortedArray() {
        ValidateRegs validateRegs = ValidateRegs.getINSTANCE();
        Registration first = new Registration("vasily", "0222", "01", "01",
                "1", currentTimeStamp(), null);
        Registration second = new Registration("ivan", "02",
                "02", "01","2", currentTimeStamp(), null);
        Registration third = new Registration("dmitry", "02",
                "02", "01","2", currentTimeStamp(), null);
        List<Registration> listOfRegistrations = new ArrayList<>();
        listOfRegistrations.add(first);
        listOfRegistrations.add(second);
        listOfRegistrations.add(third);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("listOfRegs")).thenReturn(listOfRegistrations);
        when(req.getParameter("sortWhat")).thenReturn("sortByLogin");
        when(req.getParameter("sortDirection")).thenReturn("up");
        Map<List<Registration>, Map<String, String>> result = validateRegs.sortEntries(req);
        List<Registration> actual = result.entrySet().iterator().next().getKey();
        List<Registration> expected = new ArrayList<>();
        expected.add(third);
        expected.add(second);
        expected.add(first);
        assertThat(actual, is(expected));
    }




    private String currentTimeStamp() {
        Date rawDate = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(rawDate);
    }

    private String currentTimeStampMinus1Minute() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Minus1Minute " + formatter.format(now));
        return formatter.format(now);
    }

    private String currentTimeStampPlus1Minute() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Plus1Minute " + formatter.format(now));
        return formatter.format(now);
    }

    private String yesterdayPlus1MinuteDay() {
        LocalDateTime now = LocalDateTime.now().minusDays(1).minusMinutes(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }



}