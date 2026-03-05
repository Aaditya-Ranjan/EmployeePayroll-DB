import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT e.id, e.name, p.basic_pay, e.start FROM employee e JOIN payroll p ON e.id = p.employee_id;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false", "root", "Inf@rebel1");
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                employeePayrollList.add(new EmployeePayrollData(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("basic_pay"),
                        resultSet.getDate("start").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}