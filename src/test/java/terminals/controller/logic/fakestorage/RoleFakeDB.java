package terminals.controller.logic.fakestorage;

import terminals.storage.RoleStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoleFakeDB implements RoleStorage {


    private final Map<Integer, String> roles = new TreeMap<>();


    public RoleFakeDB() {
        roles.put(1, "root");
        roles.put(2, "administrator");
        roles.put(3, "user");
    }

    @Override
    public String findRoleById(int roleId) {
        return null;
    }

    @Override
    public int findIdByRole(String role) {
        return 0;
    }

    @Override
    public List<String> findAllRoles() {
        return new ArrayList<>(roles.values());
    }
}
