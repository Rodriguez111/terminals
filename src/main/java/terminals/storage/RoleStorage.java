package terminals.storage;

import java.util.List;

public interface RoleStorage {
    String findRoleById(int roleId);

    int findIdByRole(String role);

    List<String> findAllRoles();

}
