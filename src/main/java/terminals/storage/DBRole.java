package terminals.storage;

import terminals.sql.QueryManager;
import terminals.sql.SQLManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBRole implements RoleStorage {
    private final static DBRole INSTANCE = new DBRole();

    private DBRole() {
    }




    public static DBRole getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String findRoleById(int id) {
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT role FROM roles "
                + "WHERE role_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        String result = "";
        try {
            result = queryManager.runQuery(query, params, ps -> {
                String  role = null;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    role = resultSet.getString("role");
                }
                return role;
            }).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int findIdByRole(String role) {
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT role_id FROM roles "
                + "WHERE role = ?";
        List<Object> params = new ArrayList<>();
        params.add(role);
        int result = -1;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                int  id = -1;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("role_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> findAllRoles() {
        List<String> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM roles";
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(resultSet.getString("role"));
            }
        });
        return resultList;
    }
}
