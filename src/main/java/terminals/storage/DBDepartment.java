package terminals.storage;

import terminals.models.User;
import terminals.sql.QueryManager;
import terminals.sql.SQLManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBDepartment implements DepartmentStorage {
    private final static DBDepartment INSTANCE = new DBDepartment();
    public static DBDepartment getINSTANCE() {
        return INSTANCE;
    }

    private DBDepartment() {
    }

    @Override
    public String findDepartmentById(int id) {
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT department_name FROM departments "
                + "WHERE department_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        String result = "";
        try {
            result = queryManager.runQuery(query, params, ps -> {
                String department = "";
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    department = resultSet.getString("department_name");
                }
                return department;
            }).orElse("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer findIdByDepartment(String department) {
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT department_id FROM departments "
                + "WHERE department_name =?";
        List<Object> params = new ArrayList<>();
        params.add(department);
        int result = -1;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                Integer id = null;
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    id = resultSet.getInt("department_id");
                }
                return id;
            }).orElse(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> findAllDepartments() {
        List<String> resultList = new ArrayList<>();
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "SELECT * FROM departments";
        List<Object> params = new ArrayList<>();
        queryManager.runQuery(query, params, ps -> {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                resultList.add(resultSet.getString("department_name"));
            }
        });
        return resultList;
    }

    @Override
    public String addDepartment(String department) {
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());
        String query = "INSERT INTO departments "
                + "(department_name) "
                + "VALUES (?);";
        List<Object> params = new ArrayList<>();
        params.add(department);
        long result = 0;
        try {
            result = queryManager.runQuery(query, params, ps -> {
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                return resultSet.getLong(1);

            }).orElse(-1L);
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed: departments.department_name")) {
                return "Департамент существует";
            }
            e.printStackTrace();
        }
        return String.valueOf(result);
    }

    @Override
    public String deleteDepartment(String department) {
        String result = "";
        QueryManager queryManager = new QueryManager(SQLManager.getINSTANCE().getConnection());

        String query = "DELETE FROM departments WHERE department_name=?";
        List<Object> params = new ArrayList<>();
        params.add(department);
        try {
            result = queryManager.runQuery(query, params, ps -> {
               ps.executeUpdate();
               return  "OK";
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                result = "Невозможно удалить, пока существуют зависимые записи";
            }
        }
        return result;
    }
}