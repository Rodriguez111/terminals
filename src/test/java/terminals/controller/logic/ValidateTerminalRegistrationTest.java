package terminals.controller.logic;

import org.json.JSONObject;
import org.junit.Test;
import terminals.controller.logic.fakestorage.TerminalFakeDb;
import terminals.controller.logic.fakestorage.UserFakeDb;
import terminals.models.Terminal;
import terminals.models.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ValidateTerminalRegistrationTest {
  private final static ValidateTerminalRegistration VALIDATE_TERM_REG = ValidateTerminalRegistration.getINSTANCE();
  private final static TerminalFakeDb TERMINAL_STORAGE = new TerminalFakeDb();
  private final static UserFakeDb USER_STORAGE = new UserFakeDb();

    @Test
    public void whenInvIdNotExistsThenReturnTerminalNotExistsNotification() {
        addTerminalsToDB();
        VALIDATE_TERM_REG.setTerminalStorage(TERMINAL_STORAGE);
        VALIDATE_TERM_REG.setUserStorage(USER_STORAGE);
        JSONObject result = VALIDATE_TERM_REG.validateTerminalInput("03");
        boolean actual = result.has("terminalNotExists");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenTerminalInactiveThenReturnTerminalInactiveNotification() {
        addInactiveTerminalsToDB();
        VALIDATE_TERM_REG.setTerminalStorage(TERMINAL_STORAGE);
        VALIDATE_TERM_REG.setUserStorage(USER_STORAGE);
        JSONObject result = VALIDATE_TERM_REG.validateTerminalInput("01");
        boolean actual = result.has("terminalNotActive");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

//    @Test
//    public void whenTerminalIsGivenThenReturnInfoAboutUserWhoTookIt() {
//        addGivenTerminalsToDB();
//        addUsersToDB();
//        VALIDATE_TERM_REG.setTerminalStorage(TERMINAL_STORAGE);
//        VALIDATE_TERM_REG.setUserStorage(USER_STORAGE);
//        JSONObject result = VALIDATE_TERM_REG.validateTerminalInput("01");
//        boolean actual = result.has("userLogin");
//        boolean expected = true;
//        assertThat(actual, is(expected));
//    }
//
//    @Test
//    public void whenTerminalIsNotGivenThenReturnTerminalIsReadyInfo() {
//        addGivenTerminalsToDB();
//        VALIDATE_TERM_REG.setTerminalStorage(TERMINAL_STORAGE);
//        VALIDATE_TERM_REG.setUserStorage(USER_STORAGE);
//        JSONObject result = VALIDATE_TERM_REG.validateTerminalInput("01");
//        boolean actual = result.has("terminalIsReady");
//        boolean expected = true;
//        assertThat(actual, is(expected));
//    }






    private void addTerminalsToDB() {
        Terminal terminal1 = new Terminal("01", "MC3200", "01", "01", "01", true, "", "" );
        TERMINAL_STORAGE.addTerminal(terminal1);
    }

    private void addInactiveTerminalsToDB() {
        Terminal terminal1 = new Terminal("01", "MC3200", "01", "01", "01", false, "", "" );
        TERMINAL_STORAGE.addTerminal(terminal1);
    }

    private void addGivenTerminalsToDB() {
        Terminal terminal1 = new Terminal("01", "MC3200", "01", "01", "01", true, "", "02" );
        TERMINAL_STORAGE.addTerminal(terminal1);
    }

    private void addUsersToDB() {
        User user = new User("01", "01", "01", "01", "user", "", "1", true);
        USER_STORAGE.addUser(user);
    }





}