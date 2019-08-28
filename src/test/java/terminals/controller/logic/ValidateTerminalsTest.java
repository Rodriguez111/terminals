package terminals.controller.logic;

import org.junit.Test;
import terminals.controller.logic.fakestorage.DepartmentFakeDb;
import terminals.controller.logic.fakestorage.TerminalFakeDb;
import terminals.models.Terminal;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ValidateTerminalsTest {

    @Test
    public void whenAddTerminalThenReturnOK() {
        ValidateTerminals validateTerminals = ValidateTerminals.getINSTANCE();
        validateTerminals.setTerminalStorage(new TerminalFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regId")).thenReturn("terminal01");
        when(req.getParameter("serialId")).thenReturn("serial01");
        when(req.getParameter("inventoryId")).thenReturn("inv01");
        when(req.getParameter("comment")).thenReturn("comment01");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        String actual = validateTerminals.addTerminal(req);
        String expected = "OK";
        assertThat(actual, is(expected));
    }


    @Test
    public void whenUpdateTerminalThenReturnOK() {
        ValidateTerminals validateTerminals = ValidateTerminals.getINSTANCE();
        TerminalFakeDb terminalFakeDb = new TerminalFakeDb();
        validateTerminals.setTerminalStorage(terminalFakeDb);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regId")).thenReturn("terminal01");
        when(req.getParameter("serialId")).thenReturn("serial01");
        when(req.getParameter("inventoryId")).thenReturn("inv01");
        when(req.getParameter("comment")).thenReturn("comment01");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        validateTerminals.addTerminal(req);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        when(req2.getParameter("id")).thenReturn("1");
        when(req2.getParameter("inventoryId")).thenReturn("new-inv02");
        String actual = validateTerminals.updateTerminal(req2);
        String expected = "OK";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenDeleteTerminalThenReturnOK() {
        ValidateTerminals validateTerminals = ValidateTerminals.getINSTANCE();
        TerminalFakeDb terminalFakeDb = new TerminalFakeDb();
        validateTerminals.setTerminalStorage(terminalFakeDb);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("regId")).thenReturn("terminal01");
        when(req.getParameter("serialId")).thenReturn("serial01");
        when(req.getParameter("inventoryId")).thenReturn("inv01");
        when(req.getParameter("comment")).thenReturn("comment01");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        validateTerminals.addTerminal(req);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        when(req2.getParameter("id")).thenReturn("1");
        String actual = validateTerminals.deleteTerminal(req2);
        String expected = "OK";
        assertThat(actual, is(expected));
    }


}