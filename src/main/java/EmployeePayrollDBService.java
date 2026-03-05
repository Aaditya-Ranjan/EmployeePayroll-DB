import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

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
    public void addEmployeeToPayroll(String name, double salary, LocalDate start, String gender) {
        int employeeId = -1;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service?useSSL=false&allowPublicKeyRetrieval=true", "root", "Inf@rebel1");
            connection.setAutoCommit(false); // [START TRANSACTION]

            // 1. Insert into Employee table
            String sqlEmployee = "INSERT INTO employee (company_id, name, gender, start) VALUES (1, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, gender);
                preparedStatement.setDate(3, Date.valueOf(start));
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) employeeId = rs.getInt(1);
            }

            // 2. Insert into Payroll table using the new employeeId
            String sqlPayroll = "INSERT INTO payroll (employee_id, basic_pay) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlPayroll)) {
                preparedStatement.setInt(1, employeeId);
                preparedStatement.setDouble(2, salary);
                preparedStatement.executeUpdate();
            }

            connection.commit(); // [SUCCESS - SAVE BOTH]
        } catch (SQLException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // [FAIL - UNDO ALL]
            }
            e.printStackTrace();
        }
    }
}