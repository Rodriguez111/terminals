package terminals.controller.logic;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import terminals.controller.logic.fakestorage.DepartmentFakeDb;
import terminals.controller.logic.fakestorage.RegistrationFakeDb;
import terminals.controller.logic.fakestorage.TerminalFakeDb;
import terminals.controller.logic.fakestorage.UserFakeDb;
import terminals.models.Registration;
import terminals.models.Terminal;
import terminals.models.User;
import terminals.storage.RegistrationStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ValidateUserRegistrationTest {
    private static ValidateUserRegistration validateUserReg;
    private static TerminalFakeDb terminalStorage;
    private  static UserFakeDb userStorage;
    private  static DepartmentFakeDb departmentStorage ;
    private  static RegistrationFakeDb regStorage;


    @Before
    public void initStorages() {
        validateUserReg = ValidateUserRegistration.getINSTANCE();
        terminalStorage = new TerminalFakeDb();
        userStorage = new UserFakeDb();
        departmentStorage = new DepartmentFakeDb();
        regStorage = new RegistrationFakeDb();
    }

    @Test
    public void whenGiveTerminalAndUserNotExistsThenReturnUserNotExistsNotification() {
        addUsersToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        String stringFromClient = "{\"userInputLogin\":\"user02\",\"terminalId\":\"1\",\"whoGaveLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForGiving(stringFromClient);
        boolean actual = result.has("userNotExists");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenUserNotActiveThenReturnUserNotActiveNotification() {
        addInactiveUsersToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoGaveLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForGiving(stringFromClient);
        boolean actual = result.has("userNotActive");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenUserAlreadyHaveTerminalThenReturnUserAlreadyHaveTerminalNotification() {
        addUsersWithTerminalToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        userStorage.setTerminalStorage(terminalStorage);
        addTerminalsToDB();
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoGaveLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForGiving(stringFromClient);
        boolean actual = result.has("userAlreadyHaveTerminal");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenUserDepartmentAndTerminalDepartmentNotMatchThenReturnDepartmentsNotMatchNotification() {
        addTerminalAndUserWithDifferentDepartmentsToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        userStorage.setTerminalStorage(terminalStorage);
        userStorage.setDepartmentFakeDb(departmentStorage);
        terminalStorage.setDepartmentFakeDb(departmentStorage);
        addTerminalsToDB();
        addDepartmentsToDB();
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoGaveLogin\":\"1\"}";
        JSONObject result = validateUserReg.validateUserInputForGiving(stringFromClient);
        boolean actual = result.has("departmentsNotMatch");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenUserDoNotHaveTerminalThenReturnTerminalGivingSuccessNotification() {
        addTerminalAndUserWithSameDepartmentsToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        userStorage.setTerminalStorage(terminalStorage);
        userStorage.setDepartmentFakeDb(departmentStorage);
        terminalStorage.setDepartmentFakeDb(departmentStorage);
       // addTerminalsToDB();
        addDepartmentsToDB();
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoGaveLogin\":\"1\"}";
        JSONObject result = validateUserReg.validateUserInputForGiving(stringFromClient);
        boolean actual = result.has("terminalGivingSuccess");
        boolean expected = true;
        assertThat(actual, is(expected));
    }




    @Test
    public void whenReceiveTerminalAndUserNotExistsThenReturnUserNotExistsNotification() {
        addUsersToDB();
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        String stringFromClient = "{\"userInputLogin\":\"user02\",\"terminalId\":\"1\",\"whoReceivedLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForReceiving(stringFromClient);
        boolean actual = result.has("userNotExists");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenReceiveTerminalAndUserHaveNoTerminalThenReturnUserDoNotHaveTerminalNotification() {
        addUsersToDB();
        userStorage.setTerminalStorage(terminalStorage);
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoReceivedLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForReceiving(stringFromClient);
        boolean actual = result.has("doNotHaveTerminal");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenReceiveTerminalAndUserHaveAnotherTerminalThenReturnUserNotMatchNotification() {
        add2UsersWithDifferentTerminalsToDB();
        addTerminalsToDB();
        userStorage.setTerminalStorage(terminalStorage);
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        String stringFromClient = "{\"userInputLogin\":\"user02\",\"terminalId\":\"1\",\"whoReceivedLogin\":\"01\"}";
        JSONObject result = validateUserReg.validateUserInputForReceiving(stringFromClient);
        boolean actual = result.has("userNotMatch");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenReceiveTerminalThenReturnTerminalReceivingSuccessNotification() {
        addEntryToRegistrationDB();
        add2UsersWithDifferentTerminalsToDB();
        addTerminalsToDB();
        userStorage.setTerminalStorage(terminalStorage);
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        regStorage.setUserStorage(userStorage);
        regStorage.setTerminalStorage(terminalStorage);
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoReceivedLogin\":\"user01\"}";
        JSONObject result = validateUserReg.validateUserInputForReceiving(stringFromClient);
        boolean actual = result.has("terminalReceivingSuccess");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenReceiveTerminalAndRecordNotFoundThenFatalErrorRecordNotFoundNotification() {

        add2UsersWithDifferentTerminalsToDB();
        addTerminalsToDB();
        userStorage.setTerminalStorage(terminalStorage);
        validateUserReg.setTerminalStorage(terminalStorage);
        validateUserReg.setUserStorage(userStorage);
        validateUserReg.setRegistrationStorage(regStorage);
        regStorage.setUserStorage(userStorage);
        regStorage.setTerminalStorage(terminalStorage);
        String stringFromClient = "{\"userInputLogin\":\"user01\",\"terminalId\":\"1\",\"whoReceivedLogin\":\"user01\"}";
        JSONObject result = validateUserReg.validateUserInputForReceiving(stringFromClient);
        boolean actual = result.has("fatalErrorRecordNotFound");
        boolean expected = true;
        assertThat(actual, is(expected));
    }

    private void addUsersToDB() {
        User user1 = new User("user01", "01", "01", "01", "user", "", "", true);
        userStorage.addUser(user1);
    }

    private void add2UsersWithDifferentTerminalsToDB() {
        User user1 = new User("user01", "01", "01", "01", "user", "", "terminal01", true);
        User user2 = new User("user02", "02", "02", "02", "user", "", "terminal02", true);
        userStorage.addUser(user1);
        userStorage.addUser(user2);
    }


    private void addInactiveUsersToDB() {
        User user1 = new User("user01", "01", "01", "01", "user", "", "", false);
        userStorage.addUser(user1);
    }

    private void addUsersWithTerminalToDB() {
        User user1 = new User("user01", "01", "01", "01", "user", "", "terminal02", true);
        userStorage.addUser(user1);
    }

    private void addTerminalsToDB() {
        Terminal terminal1 = new Terminal("terminal01", "01", "01", "01", true, "", "" );
        Terminal terminal2 = new Terminal("terminal02", "02", "02", "02", true, "", "" );
        terminalStorage.addTerminal(terminal1);
        terminalStorage.addTerminal(terminal2);
    }

    private void addTerminalAndUserWithDifferentDepartmentsToDB() {
        Terminal terminal1 = new Terminal("terminal01", "01", "01", "01", true, "dep1", "" );
        User user1 = new User("user01", "01", "01", "01", "user", "dep2", "", true);
        userStorage.addUser(user1);
        terminalStorage.addTerminal(terminal1);
    }

    private void addTerminalAndUserWithSameDepartmentsToDB() {
        Terminal terminal1 = new Terminal("terminal01", "01", "01", "01", true, "dep1", "" );
        User user1 = new User("user01", "01", "01", "01", "user", "dep1", "", true);
        userStorage.addUser(user1);
        terminalStorage.addTerminal(terminal1);
    }

    private void addEntryToRegistrationDB() {
        Registration registration = new Registration("user01", "terminal01");
        regStorage.addEntry(registration);
    }


    private void addDepartmentsToDB() {
        departmentStorage.addDepartment("dep1");
        departmentStorage.addDepartment("dep2");
    }
}