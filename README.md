# Employee Payroll Service — Java JDBC

A Java application that manages employee payroll data using JDBC and a MySQL backend. The project covers full CRUD operations, ER modeling, database normalization, and SQL query execution via the Java JDBC API.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Database Setup](#database-setup)
- [Schema](#schema)
- [Use Cases](#use-cases)
- [JDBC Architecture](#jdbc-architecture)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)

---

## Overview

This project demonstrates how to design and interact with a relational MySQL database from a Java application. It walks through creating the `payroll_service` database, building a normalized schema based on an ER diagram, and performing all CRUD operations using JDBC `Statement` and `PreparedStatement`.

---

## Tech Stack

- Java (JDK 8+)
- MySQL 5.7+
- JDBC (MySQL Connector/J)
- JUnit (for testing)
- Maven or Gradle (dependency management)

---

## Database Setup

```sql
-- Create and select the database
CREATE DATABASE payroll_service;
USE payroll_service;

-- Create the employee_payroll table
CREATE TABLE employee_payroll (
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name         VARCHAR(150) NOT NULL,
    phone_number VARCHAR(250),
    address      VARCHAR(250) DEFAULT 'TBD',
    department   VARCHAR(150) NOT NULL,
    gender       CHAR(1),
    basic_pay    DOUBLE NOT NULL,
    deductions   DOUBLE NOT NULL,
    taxable_pay  DOUBLE NOT NULL,
    tax          DOUBLE NOT NULL,
    net_pay      DOUBLE NOT NULL,
    start        DATE NOT NULL,
    PRIMARY KEY (id)
);
```

---

## Schema

The final schema is derived from an ER diagram with the following entities and relationships:

| Entity | Relationship | Entity |
|---|---|---|
| Company | One-to-Many | Employee |
| Employee | Many-to-Many | Department |
| Employee | One-to-One | Payroll |

For the Many-to-Many relationship between Employee and Department, a junction table `employee_department` is used with `employee_id` and `department_id` columns.

---

## Use Cases

### Section 1 — MySQL DB

| UC | Description |
|---|---|
| UC1 | Create the `payroll_service` database |
| UC2 | Create the `employee_payroll` table with id, name, salary, start date |
| UC3 | Insert employee payroll records using `INSERT INTO` |
| UC4 | Retrieve all records using `SELECT * FROM employee_payroll` |
| UC5 | Retrieve salary by employee name and filter by date range using `BETWEEN` |
| UC6 | Add a `gender` column via `ALTER TABLE` and update rows |
| UC7 | Aggregate salary stats (SUM, AVG, MIN, MAX, COUNT) grouped by gender |

### Section 2 — ER Diagram

| UC | Description |
|---|---|
| UC8 | Extend the table with `phone_number`, `address`, and `department` |
| UC9 | Add payroll detail columns: `basic_pay`, `deductions`, `taxable_pay`, `tax`, `net_pay` |
| UC10 | Identify data redundancy problems; design ER diagram to resolve them |
| UC11 | Implement the ER diagram into the DB — create normalized tables |
| UC12 | Validate that UC4, UC5, and UC7 queries work with the new normalized structure |

### Section 3 — JDBC

| UC | Description |
|---|---|
| UC1 | Connect Java application to the MySQL `payroll_service` database |
| UC2 | Retrieve all employee payroll records via JDBC into `EmployeePayrollData` objects |
| UC3 | Update Terisa's salary to `3000000.00` using JDBC `Statement` |
| UC4 | Refactor UC3 to use JDBC `PreparedStatement`; implement Singleton pattern for DB service |
| UC5 | Retrieve employees who joined within a given date range |
| UC6 | Perform salary aggregation (SUM, AVG, MIN, MAX, COUNT) grouped by gender via JDBC |

---

## JDBC Architecture

```
Java App
   │
   ├── DriverManager.getConnection(url, user, password)
   │         └── Connection
   │               ├── createStatement()          → Statement
   │               └── prepareStatement(sql)      → PreparedStatement (cached, reusable)
   │
   └── ResultSet  ←  executeQuery() / executeUpdate()
```

**Key classes:**
- `DriverManager` — loads and manages JDBC drivers
- `Connection` — represents an active DB session
- `Statement` — executes plain SQL queries
- `PreparedStatement` — executes parameterized SQL; cached at driver and DB level for better performance
- `ResultSet` — iterates over rows returned by a SELECT query

**Connection string:**
```
jdbc:mysql://localhost:3306/payroll_service?useSSL=false
```

---

## Getting Started

### Prerequisites

- MySQL installed and running locally
- Java JDK 8 or above
- Maven or Gradle

### Maven Dependency

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Running the App

1. Start MySQL and run the database setup SQL above.
2. Clone this repository and open in your IDE (Eclipse/IntelliJ).
3. Update the DB credentials in the service class:
   ```java
   String jdbcURL  = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
   String userName = "root";
   String password = "your_password";
   ```
4. Run the main class or JUnit tests.

---

## Project Structure

```
src/
├── main/java/
│   ├── EmployeePayrollData.java       # POJO for employee payroll records
│   ├── EmployeePayrollService.java    # Business logic layer
│   ├── EmployeePayrollDBService.java  # Singleton — JDBC DB operations
│   └── EmployeePayrollException.java # Custom exception class
└── test/java/
    └── EmployeePayrollServiceTest.java # JUnit tests for all use cases
```

---

## Sample Queries

```sql
-- Retrieve Bill's salary
SELECT salary FROM employee_payroll WHERE name = 'Bill';

-- Employees joined between two dates
SELECT * FROM employee_payroll
WHERE start BETWEEN CAST('2018-01-01' AS DATE) AND DATE(NOW());

-- Salary stats grouped by gender
SELECT gender, SUM(salary), AVG(salary), MIN(salary), MAX(salary), COUNT(*)
FROM employee_payroll
GROUP BY gender;
```
