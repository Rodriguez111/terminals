package terminals.controller.logic;

import org.junit.Test;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ValidateRolesTest {
    @Test
    public void whenGetAllRolesThenReturnRolesWithoutRoot() {
        ValidateRoles validateRoles = ValidateRoles.getINSTANCE();
       List<String> result = validateRoles.findAllRoles();
       int actual = result.size();
       int expected = 2;
       assertThat(actual, is(expected));
    }

}