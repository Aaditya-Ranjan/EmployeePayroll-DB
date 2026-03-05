import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {
    public static void main(String[] args) {
        // Update URL, username, and password as per your local MySQL setup [cite: 223-224]
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false"; // [cite: 233]
        String userName = "root"; // [cite: 234]
        String password = "Inf@rebel1"; // [cite: 235]
        Connection con;

        try {
            // Check if Driver Class is available [cite: 219, 225, 238]
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e); // [cite: 241]
        }

        listDrivers(); // [cite: 242]

        try {
            System.out.println("Connecting to database: " + jdbcURL); // [cite: 244]
            con = DriverManager.getConnection(jdbcURL, userName, password); // [cite: 227, 245]
            System.out.println("Connection is successful!!!!!! " + con); //
        } catch (Exception e) {
            e.printStackTrace(); // [cite: 247]
        }
    }

    private static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers(); // [cite: 252]
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement(); // [cite: 253]
            System.out.println(" " + driverClass.getClass().getName()); // [cite: 253]
        }
    }
}