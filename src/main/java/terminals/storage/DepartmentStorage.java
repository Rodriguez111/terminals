package terminals.storage;



import java.util.List;

public interface DepartmentStorage {
    String findDepartmentById(int departmentId);

    Integer findIdByDepartment(String department);

    List<String> findAllDepartments();

    String addDepartment(String department);

    String deleteDepartment(String department);
}
