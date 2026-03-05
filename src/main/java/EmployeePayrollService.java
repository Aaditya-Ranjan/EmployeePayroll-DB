import java.util.List;
import java.time.LocalDate;
import java.util.*;
public class EmployeePayrollService {
    private List<EmployeePayrollData> employeePayrollList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = new EmployeePayrollDBService();
    }

    public List<EmployeePayrollData> readEmployeePayrollData() {
        this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }
    public List<EmployeePayrollData> readEmployeePayrollDataForDateRange(LocalDate startDate, LocalDate endDate) {
        return employeePayrollDBService.getEmployeePayrollDataByDateRange(startDate, endDate);
    }

    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();
        List<EmployeePayrollData> data = service.readEmployeePayrollData();
        data.forEach(System.out::println);
    }
    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary); //
        if (result == 0) return; //
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name); //
        if (employeePayrollData != null) employeePayrollData.salary = salary; //
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employee -> employee.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.readData(); //
        EmployeePayrollData dbData = employeePayrollDataList.stream()
                .filter(employee -> employee.name.equals(name))
                .findFirst()
                .orElse(null);
        return dbData.salary == (this.getEmployeePayrollData(name).salary); //
    }
    // Add this method to bridge the Test and the DB Service
    public void addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        employeePayrollDBService.addEmployeeToPayroll(name, salary, start, gender);
    }
    public Map<String, Double> readAverageSalaryByGender() {
        return employeePayrollDBService.getAverageSalaryByGender();
    }
    // This connects your Test to your DB Service logic
    public Map<String, Double> getAverageSalaryByGender() {
        return employeePayrollDBService.getAverageSalaryByGender();
    }
}