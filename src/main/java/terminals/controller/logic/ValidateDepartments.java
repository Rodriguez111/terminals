package terminals.controller.logic;

import terminals.storage.DBDepartment;
import terminals.storage.DepartmentStorage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public class ValidateDepartments implements DepartmentsValidator {
    private final static ValidateDepartments INSTANCE = new ValidateDepartments();

    private DepartmentStorage departmentStorage = DBDepartment.getINSTANCE();

    private ValidateDepartments() {
    }

    public void setDepartmentStorage(DepartmentStorage departmentStorage) {
        this.departmentStorage = departmentStorage;
    }

    public static ValidateDepartments getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public List<String> findAllDepartments() {
        List<String> listOfDepartments = departmentStorage.findAllDepartments();
        listOfDepartments.add("");
        return listOfDepartments;
    }


    @Override
    public String addDepartment(HttpServletRequest request) {
        return departmentStorage.addDepartment(request.getParameter("department"));
    }

    @Override
    public String deleteDepartment(HttpServletRequest request) {
      return departmentStorage.deleteDepartment(request.getParameter("department"));
    }


}
