import java.time.LocalDate;

public class EmployeePayrollData {
    public int id;
    public String name;
    public double salary;
    public LocalDate start;

    public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.start = start;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Salary: " + salary + ", Start Date: " + start;
    }
}