package terminals.controller.logic;

import org.junit.Test;
import terminals.controller.logic.fakestorage.UserFakeDb;
import terminals.models.User;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ValidateUsersTest {

    @Test
    public void whenAddUserThenReturnOK() {
        ValidateUsers validateUsers = ValidateUsers.getINSTANCE();
        validateUsers.setUserStorage(new UserFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("login")).thenReturn("user01");
        when(req.getParameter("password")).thenReturn("pass01");
        when(req.getParameter("name")).thenReturn("userName");
        when(req.getParameter("surname")).thenReturn("userSurname");
        when(req.getParameter("role")).thenReturn("user");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        String actual = validateUsers.addUser(req);
        String expected = "OK";
        assertThat(actual, is(expected));
    }


//    @Test
//    public void whenUpdateUserThenReturnOK() {
//        ValidateUsers validateUsers = ValidateUsers.getINSTANCE();
//        validateUsers.setUserStorage(new UserFakeDb());
//        HttpServletRequest req = mock(HttpServletRequest.class);
//        when(req.getParameter("login")).thenReturn("user01");
//        when(req.getParameter("password")).thenReturn("pass01");
//        when(req.getParameter("name")).thenReturn("userName");
//        when(req.getParameter("surname")).thenReturn("userSurname");
//        when(req.getParameter("role")).thenReturn("user");
//        when(req.getParameter("department")).thenReturn("dep1");
//        when(req.getParameter("isActive")).thenReturn("1");
//        validateUsers.addUser(req);
//        HttpServletRequest req2 = mock(HttpServletRequest.class);
//        when(req2.getParameter("id")).thenReturn("1");
//        when(req2.getParameter("name")).thenReturn("new-userName");
//        String actual = validateUsers.updateUser(req2);
//        String expected = "OK";
//        assertThat(actual, is(expected));
//    }

    @Test
    public void whenDeleteTerminalThenReturnOK() {
        ValidateUsers validateUsers = ValidateUsers.getINSTANCE();
        validateUsers.setUserStorage(new UserFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("login")).thenReturn("user01");
        when(req.getParameter("password")).thenReturn("pass01");
        when(req.getParameter("name")).thenReturn("userName");
        when(req.getParameter("surname")).thenReturn("userSurname");
        when(req.getParameter("role")).thenReturn("user");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        validateUsers.addUser(req);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        when(req2.getParameter("id")).thenReturn("1");
        String actual = validateUsers.deleteUser(req2);
        String expected = "OK";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenLoginAndPassMatchAndUserIsAdminThenReturnUser() {
        ValidateUsers validateUsers = ValidateUsers.getINSTANCE();
        validateUsers.setUserStorage(new UserFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("login")).thenReturn("user01");
        when(req.getParameter("password")).thenReturn("pass01");
        when(req.getParameter("name")).thenReturn("userName");
        when(req.getParameter("surname")).thenReturn("userSurname");
        when(req.getParameter("role")).thenReturn("administrator");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        validateUsers.addUser(req);
        User user = validateUsers.checkUserCanLogin("user01", "pass01");
        assertNotNull(user);
    }

    @Test
    public void whenLoginAndPassMatchAndUserIsNotAdminThenReturnNull() {
        ValidateUsers validateUsers = ValidateUsers.getINSTANCE();
        validateUsers.setUserStorage(new UserFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("login")).thenReturn("user01");
        when(req.getParameter("password")).thenReturn("pass01");
        when(req.getParameter("name")).thenReturn("userName");
        when(req.getParameter("surname")).thenReturn("userSurname");
        when(req.getParameter("role")).thenReturn("user");
        when(req.getParameter("department")).thenReturn("dep1");
        when(req.getParameter("isActive")).thenReturn("1");
        validateUsers.addUser(req);
        User user = validateUsers.checkUserCanLogin("user01", "pass01");
        assertNull(user);
    }


}