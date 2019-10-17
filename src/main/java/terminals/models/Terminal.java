package terminals.models;

import java.util.Objects;

public class Terminal implements Cloneable {
    private int id;
    private String regId;
    private String terminalModel;
    private String serialId;
    private String inventoryId;
    private String terminalComment;
    private boolean terminalIsActive;
    private String departmentName;
    private String userLogin;
    private String createDate;
    private String lastUpdateDate;

    public Terminal(String regId, String terminalModel, String serialId, String inventoryId, String terminalComment, boolean terminalIsActive,
                    String departmentName, String userLogin) {
        this.regId = regId;
        this.terminalModel = terminalModel;
        this.serialId = serialId;
        this.inventoryId = inventoryId;
        this.terminalComment = terminalComment;
        this.terminalIsActive = terminalIsActive;
        this.departmentName = departmentName;
        this.userLogin = userLogin;
    }

    public Terminal(String regId, String terminalModel, String serialId, String inventoryId, String terminalComment, boolean terminalIsActive,
                    String departmentName) {
        this.regId = regId;
        this.terminalModel = terminalModel;
        this.serialId = serialId;
        this.inventoryId = inventoryId;
        this.terminalComment = terminalComment;
        this.terminalIsActive = terminalIsActive;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getTerminalComment() {
        return terminalComment;
    }

    public void setTerminalComment(String terminalComment) {
        this.terminalComment = terminalComment;
    }

    public boolean isTerminalIsActive() {
        return terminalIsActive;
    }

    public void setTerminalIsActive(boolean terminalIsActive) {
        this.terminalIsActive = terminalIsActive;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getTerminalModel() {
        return terminalModel;
    }

    public void setTerminalModel(String terminalModel) {
        this.terminalModel = terminalModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal terminal = (Terminal) o;
        return terminalIsActive == terminal.terminalIsActive &&
                regId.equals(terminal.regId) &&
                terminalModel.equals(terminal.terminalModel) &&
                serialId.equals(terminal.serialId) &&
                inventoryId.equals(terminal.inventoryId) &&
                Objects.equals(terminalComment, terminal.terminalComment) &&
                Objects.equals(departmentName, terminal.departmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regId, terminalModel, serialId, inventoryId, terminalComment, terminalIsActive, departmentName);
    }

    @Override
    public Object clone() {
        Terminal terminal = null;
        try {
            terminal = (Terminal)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return terminal;
    }
}
