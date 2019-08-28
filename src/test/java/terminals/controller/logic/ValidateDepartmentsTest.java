package terminals.controller.logic;

import org.junit.Test;
import terminals.controller.logic.fakestorage.DepartmentFakeDb;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ValidateDepartmentsTest {


    @Test
    public void whenAddDepartmentThenReturn1() {
        ValidateDepartments validateDepartments = ValidateDepartments.getINSTANCE();
        validateDepartments.setDepartmentStorage(new DepartmentFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("department")).thenReturn("NewTestDepartment");
        String actual = validateDepartments.addDepartment(req);
        String expected = "1";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenAddDepartmentWithSameNameThenReturnDepartmentExists() {
        ValidateDepartments validateDepartments = ValidateDepartments.getINSTANCE();
        validateDepartments.setDepartmentStorage(new DepartmentFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        when(req.getParameter("department")).thenReturn("NewTestDepartment");
        when(req2.getParameter("department")).thenReturn("NewTestDepartment");
        validateDepartments.addDepartment(req);
        String actual = validateDepartments.addDepartment(req2);
        String expected = "Департамент существует";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenFindAllDepartmentsThenGetDepartmentsListWithEmptyStringInLastIndex() {
        ValidateDepartments validateDepartments = ValidateDepartments.getINSTANCE();
        validateDepartments.setDepartmentStorage(new DepartmentFakeDb());
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletRequest req2 = mock(HttpServletRequest.class);
        when(req.getParameter("department")).thenReturn("NewTestDepartment1");
        when(req2.getParameter("department")).thenReturn("NewTestDepartment2");
        validateDepartments.addDepartment(req);
        validateDepartments.addDepartment(req2);
        List<String> expected = new ArrayList<>();
        expected.add("NewTestDepartment1");
        expected.add("NewTestDepartment2");
        expected.add("");
        List<String> actual = validateDepartments.findAllDepartments();
        assertThat(actual, is(expected));
    }

}