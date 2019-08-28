package terminals.controller.logic.fakestorage;

import terminals.models.Terminal;
import terminals.sql.DataType;
import terminals.storage.TerminalStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminalFakeDb implements TerminalStorage {

    private final static AtomicInteger COUNT = new AtomicInteger();

    private final Map<Integer, Terminal> terminals = new TreeMap<>();

    private DepartmentFakeDb departmentFakeDb;

    public void setDepartmentFakeDb(DepartmentFakeDb departmentFakeDb) {
        this.departmentFakeDb = departmentFakeDb;
    }

    @Override
    public List<Terminal> findAllTerminals() {
        return new ArrayList<>(terminals.values());
    }

    @Override
    public Terminal findTerminalById(int id) {

        return terminals.get(id);
    }

    @Override
    public int findIdByRegId(String regId) {
        Integer result = 0;
        for(Map.Entry<Integer, Terminal> each : terminals.entrySet()) {
            if(each.getValue().getRegId().equals(regId)) {
                result = each.getKey();
            }
        }
        return result;
    }

    @Override
    public String findFieldById(int id, String fieldName, DataType dataType) {
        String result = "";
        Terminal terminal = terminals.get(id);
        if (fieldName.equals("terminal_reg_id")) {
            result =  terminal.getRegId();
        }
        if (fieldName.equals("terminal_is_active")) {
            result =  terminal.isTerminalIsActive()? "1" : "0";
        }
        if (fieldName.equals("terminal_department_id")) {
            String department = terminal.getDepartmentName();
            result = String.valueOf(departmentFakeDb.findIdByDepartment(department));
        }

        return result;
    }

    @Override
    public String addTerminal(Terminal terminal) {
        Terminal terminal1 = new Terminal(terminal.getRegId(), terminal.getSerialId(), terminal.getInventoryId(),
                terminal.getTerminalComment(), terminal.isTerminalIsActive(), terminal.getDepartmentName(), "");
        terminals.put(COUNT.incrementAndGet(), terminal1);
        return "OK";
    }

    @Override
    public String addTerminalWithDepartment(Terminal terminal) {
        Terminal terminal1 = new Terminal(terminal.getRegId(), terminal.getSerialId(), terminal.getInventoryId(),
                terminal.getTerminalComment(), terminal.isTerminalIsActive(), terminal.getDepartmentName(), "");
        terminals.put(COUNT.incrementAndGet(), terminal1);
        return "OK";
    }

    @Override
    public String updateTerminal(Terminal terminal) {
        terminals.put(terminal.getId(), terminal);
        return "OK";
    }

    @Override
    public String updateTerminalWithDepartment(Terminal terminal) {
        terminals.put(terminal.getId(), terminal);
        return "OK";
    }

    @Override
    public String deleteTerminal(int id) {
        terminals.remove(id);
        return "OK";
    }

    @Override
    public int findIdByField(Map<String, String> parameters) {
        int result = -1;
        for(Map.Entry<Integer, Terminal> eachEntry : terminals.entrySet()) {
            if (parameters.get("terminal_inventory_id") != null) {
                if(eachEntry.getValue().getInventoryId().equals(parameters.get("terminal_inventory_id"))) {
                    result =  eachEntry.getKey();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public int countOfTerminals(String whatToCount) {
        return 0;
    }

    @Override
    public void addUserToTerminal(int terminalId, int userId) {

    }

    @Override
    public void removeUserFromTerminal(int terminalId) {

    }
}
