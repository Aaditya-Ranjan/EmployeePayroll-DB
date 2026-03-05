import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EmployeePayrollServiceTest {
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService service = new EmployeePayrollService();
        service.readEmployeePayrollData();
        service.updateEmployeeSalary("Terissa", 3000000.00); // [cite: 289]
        boolean result = service.checkEmployeePayrollInSyncWithDB("Terissa"); // 
        Assertions.assertTrue(result);
    }
}