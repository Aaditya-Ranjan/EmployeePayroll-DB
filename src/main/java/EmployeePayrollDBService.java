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
    public int updateEmployeeData(String name, double salary) {
        // Note: In normalized schema, we update the 'payroll' table joined on 'employee'
        String sql = String.format("UPDATE payroll p JOIN employee e ON e.id = p.employee_id " +
                "SET p.basic_pay = %.2f WHERE e.name = '%s';", salary, name);
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false", "root", "Inf@rebel1");
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}