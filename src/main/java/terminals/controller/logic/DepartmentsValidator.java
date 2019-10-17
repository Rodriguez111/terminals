package terminals.controller.logic;

import terminals.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DepartmentsValidator {

    List<String> findAllDepartments();

    String addDepartment(HttpServletRequest request);

    String renameDepartment(String oldDepName, String newDepName);

    String deleteDepartment(HttpServletRequest request);
}
