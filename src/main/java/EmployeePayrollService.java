import java.util.List;

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

    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();
        List<EmployeePayrollData> data = service.readEmployeePayrollData();
        data.forEach(System.out::println);
    }
}