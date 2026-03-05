import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.*;

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
    public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        // SQL query using '?' placeholders for security
        String sql = "UPDATE payroll p JOIN employee e ON e.id = p.employee_id " +
                "SET p.basic_pay = ? WHERE e.name = ?;";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false", "root", "Inf@rebel1");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) { //

            // Binding values to the '?' placeholders
            preparedStatement.setDouble(1, salary); // First '?'
            preparedStatement.setString(2, name);   // Second '?'

            return preparedStatement.executeUpdate(); //
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<EmployeePayrollData> getEmployeePayrollDataByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT e.id, e.name, p.basic_pay, e.start FROM employee e " +
                "JOIN payroll p ON e.id = p.employee_id " +
                "WHERE e.start BETWEEN ? AND ?;"; //
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false", "root", "Inf@rebel1");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate)); //
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));   //

            ResultSet resultSet = preparedStatement.executeQuery();
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
    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "SELECT e.gender, AVG(p.basic_pay) as avg FROM employee e JOIN payroll p ON e.id = p.employee_id GROUP BY e.gender;";
        Map<String, Double> map = new HashMap<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false&allowPublicKeyRetrieval=true", "root", "Inf@rebel1")) {
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) map.put(rs.getString("gender"), rs.getDouble("avg"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
}