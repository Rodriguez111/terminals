package terminals.controller.logic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terminals.models.Terminal;
import terminals.storage.DBTerminal;
import terminals.storage.TerminalStorage;


import javax.servlet.http.HttpServletRequest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ValidateTerminals implements TerminalsValidator {

//    private static final Logger LOG = LoggerFactory.getLogger(ValidateTerminals.class);
    private final static ValidateTerminals INSTANCE = new ValidateTerminals();
    private TerminalStorage terminalStorage = DBTerminal.getINSTANCE();

    private ValidateTerminals() {
    }

    public static ValidateTerminals getINSTANCE() {
        return INSTANCE;
    }

    public void setTerminalStorage(TerminalStorage terminalStorage) {
        this.terminalStorage = terminalStorage;
    }

    @Override
    public List<Terminal> findAllTerminals() {
        List<Terminal> listOfTerminals = terminalStorage.findAllTerminals();
        listOfTerminals.sort(new Comparator<Terminal>() {
            @Override
            public int compare(Terminal o1, Terminal o2) {
                Integer first = 0;
                int second = 0;
                try{
                    first = Integer.parseInt(o1.getRegId().substring(2));
                    second = Integer.parseInt(o2.getRegId().substring(2));
                } catch (NumberFormatException e) {
//                    LOG.error("Некорректный формат учетного номера терминала");
                    System.out.println("Некорректный формат учетного номера терминала");
                    e.printStackTrace();
                }

                return first.compareTo(second);
            }
        });
        return listOfTerminals;
    }

    @Override
    public List<Terminal> findActiveTerminals() {
        List<Terminal> listOfTerminals = findAllTerminals();
        return listOfTerminals.stream().filter(Terminal::isTerminalIsActive).collect(Collectors.toList());
    }

    @Override
    public Terminal findTerminalById(int id) {
        return terminalStorage.findTerminalById(id);
    }

    @Override
    public String addTerminal(HttpServletRequest request) {
//        LOG.info("Enter method");
        String resultMessage;
        String terminalRegId = request.getParameter("regId");
        String terminalModel = request.getParameter("model");
        String terminalSerialId = request.getParameter("serialId");
        String terminalInventoryId = request.getParameter("inventoryId");
        String terminalComment = request.getParameter("comment").isEmpty() ? "" : request.getParameter("comment");
        String department = request.getParameter("department");
        boolean terminalIsActive = request.getParameter("isActive")!= null;

        Terminal terminal = new Terminal(terminalRegId, terminalModel, terminalSerialId, terminalInventoryId, terminalComment, terminalIsActive, department);
        if (department.equals("")) {
            resultMessage =  terminalStorage.addTerminal(terminal);
        } else {
            resultMessage =  terminalStorage.addTerminalWithDepartment(terminal);
        }
//        LOG.info("Exit method");
        return resultMessage;
    }

    @Override
    public String updateTerminal(HttpServletRequest request) {
//        LOG.info("Enter method");
        String result = "OK";
        Terminal terminal = findTerminalById(Integer.parseInt(request.getParameter("id")));
        if(terminal != null) {
            Terminal updatedTerminal = updateTerminalFields(terminal, request);
            if(terminal.equals(updatedTerminal)) {
                result = "Нет полей для обновления";
            }
            else if(terminal.isTerminalIsActive() != updatedTerminal.isTerminalIsActive() && !terminal.getUserLogin().isEmpty()) {
                result = "Нельзя деактивировать терминал, пока он выдан пользователю";
            }
            else if(!terminal.getDepartmentName().equals(updatedTerminal.getDepartmentName()) && !terminal.getUserLogin().isEmpty()) {
                result = "Нельзя сменить департамент терминала, пока он выдан пользователю";
            }
            else {
                if (updatedTerminal.getDepartmentName().equals("")) {
                    result = terminalStorage.updateTerminal(updatedTerminal);
                } else {
                    result = terminalStorage.updateTerminalWithDepartment(updatedTerminal);
                }
            }
//            LOG.info("Exit method");
        }
        return result;
    }

    @Override
    public String deleteTerminal(HttpServletRequest request) {
        return terminalStorage.deleteTerminal(Integer.parseInt(request.getParameter("id")));
    }


    private Terminal updateTerminalFields (Terminal terminal, HttpServletRequest request) {
//        LOG.info("Enter method");
        Terminal updatedTerminal = (Terminal)terminal.clone();
        if(validateField(request.getParameter("regId"))) {
            updatedTerminal.setRegId(request.getParameter("regId"));
        }
        if(validateField(request.getParameter("model"))) {
            updatedTerminal.setTerminalModel(request.getParameter("model"));
        }
        if(validateField(request.getParameter("serialId"))) {
            updatedTerminal.setSerialId(request.getParameter("serialId"));
        }
        if(validateField(request.getParameter("inventoryId"))) {
            updatedTerminal.setInventoryId(request.getParameter("inventoryId"));
        }
        if(validateField(request.getParameter("comment"))) {
            updatedTerminal.setTerminalComment(request.getParameter("comment"));
        }
        if(request.getParameter("department") != null) {
           updatedTerminal.setDepartmentName(request.getParameter("department"));
        }
        updatedTerminal.setTerminalIsActive(request.getParameter("isActive") != null);
//        LOG.info("Exit method");
        return updatedTerminal;
    }

    private boolean validateField(String value) {
//        LOG.info("Enter and exit method");
        return value != null && !value.equals("");
    }

    @Override
    public int getCountOfAllTerminals() {
        return terminalStorage.countOfTerminals(null);
    }

    @Override
    public int getCountOfActiveTerminals() {
        int inactive = terminalStorage.countOfTerminals("inactive");
        int all = getCountOfAllTerminals();
        return all - inactive;
    }
}
