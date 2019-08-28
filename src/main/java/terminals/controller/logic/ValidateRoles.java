package terminals.controller.logic;

import terminals.storage.DBRole;
import terminals.storage.RoleStorage;

import java.util.List;

public class ValidateRoles implements RolesValidator {
    private final static ValidateRoles INSTANCE = new ValidateRoles();

    private RoleStorage roleStorage = DBRole.getINSTANCE();

    private ValidateRoles() {
    }

    public static ValidateRoles getINSTANCE() {
        return INSTANCE;
    }

    public void setRoleStorage(RoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }

    @Override
    public List<String> findAllRoles() {
        List<String> roles = roleStorage.findAllRoles();
        roles.remove("root");
        return roles;
    }
}
