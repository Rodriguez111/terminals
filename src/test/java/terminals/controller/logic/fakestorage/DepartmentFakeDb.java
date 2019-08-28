package terminals.controller.logic.fakestorage;

import terminals.storage.DepartmentStorage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DepartmentFakeDb implements DepartmentStorage {

    private final static AtomicInteger COUNT = new AtomicInteger();

    private final Map<Integer, String> departments = new TreeMap<>();


    @Override
    public String findDepartmentById(int departmentId) {
        return departments.entrySet().stream().filter(entry -> departmentId == entry.getKey()).map(Map.Entry::getValue).findFirst().get();
    }

    @Override
    public Integer findIdByDepartment(String department) {
        return departments.entrySet().stream().filter(entry -> department.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().get();
    }

    @Override
    public List<String> findAllDepartments() {
        return new ArrayList<>(departments.values());
    }

    @Override
    public String addDepartment(String department) {
        String result = "";
        if(departments.containsValue(department)) {
            result = "Департамент существует";
        } else {
            int index = COUNT.incrementAndGet();
            this.departments.put(index, department);
            result =  String.valueOf(index);
        }
        return result;
    }

    @Override
    public String deleteDepartment(String department) {
        this.departments.remove(department);
        return "OK";
    }
}
