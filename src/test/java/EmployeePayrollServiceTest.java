import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.time.LocalDate;

public class EmployeePayrollServiceTest {
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
        EmployeePayrollService service = new EmployeePayrollService();
        service.readEmployeePayrollData();
        service.updateEmployeeSalary("Terissa", 3000000.00); // [cite: 289]
        boolean result = service.checkEmployeePayrollInSyncWithDB("Terissa"); // 
        Assertions.assertTrue(result);
    }
    @Test
    public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService service = new EmployeePayrollService();
        LocalDate startDate = LocalDate.of(2018, 01, 01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollDataList =
                service.readEmployeePayrollDataForDateRange(startDate, endDate); //

        // Check if the number of employees returned matches your DB entries (e.g., 3)
        Assertions.assertEquals(3, employeePayrollDataList.size());
    }
}