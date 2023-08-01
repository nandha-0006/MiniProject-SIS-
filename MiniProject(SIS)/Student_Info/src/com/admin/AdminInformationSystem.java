package com.admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

//class abstraction
public class AdminInformationSystem implements UserSystem {
    private static String DB_URL = "jdbc:mysql://localhost:3306/mini?user=root&password=tiger";
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AdminInformationSystem adminSystem = new AdminInformationSystem("jdbc:mysql://localhost:3306/mini?user=root&password=tiger");

        try {
            if (adminSystem.login()) {
                adminSystem.createTableIfNotExists();

                while (true) {
                    adminSystem.displayMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    adminSystem.performAction(choice);
                }
            } else {
                System.out.println("Admin login failed. Exiting the program.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Polymorphism
    @Override
    public boolean login() {
        System.out.println("Admin Login:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            return AdminLoginTable.isAdminLoginValid(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createTableIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "course TEXT NOT NULL, " +
                    "grade REAL NOT NULL)";
            stmt.execute(sql);

            String sqlTeachers = "CREATE TABLE IF NOT EXISTS teacher (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL)";
            stmt.execute(sqlTeachers);
        }
    }

    @Override
    public void displayMenu() {
        System.out.println("\nStudent Information System");
        System.out.println("1. Add Student");
        System.out.println("2. Display Students");
        System.out.println("3. Update Student Grade");
        System.out.println("4. Delete Student");
        // Option to add a teacher.
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    @Override
    public void performAction(int choice) {
        switch (choice) {
            case 1:
                try {
                    addStudent();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    displayStudents();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    updateStudentGrade();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    deleteStudent();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                System.out.println("Exiting the program.");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public AdminInformationSystem(String dbUrl) {
        DB_URL = dbUrl;
    }

    public static void addStudent() throws SQLException {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter course: ");
        String course = scanner.nextLine();

        System.out.print("Enter grade: ");
        double grade = scanner.nextDouble();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students (name, course, grade) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, course);
            pstmt.setDouble(3, grade);
            pstmt.executeUpdate();
        }

        System.out.println("Student added successfully!");
    }

    public static void displayStudents() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM students")) {
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No students found.");
            } else {
                System.out.println("\nStudent List:");
                System.out.println("------------------------------------------------------------");
                System.out.printf("%-5s %-20s %-20s %-5s%n", "ID", "Name", "Course", "Grade");
                System.out.println("------------------------------------------------------------");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String course = resultSet.getString("course");
                    double grade = resultSet.getDouble("grade");
                    System.out.printf("%-5d %-20s %-20s %-5.2f%n", id, name, course, grade);
                }
                System.out.println("------------------------------------------------------------");
            }
        }
    }

    public static void updateStudentGrade() throws SQLException {
        System.out.print("Enter student ID to update grade: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new grade: ");
        double newGrade = scanner.nextDouble();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE students SET grade = ? WHERE id = ?")) {
            pstmt.setDouble(1, newGrade);
            pstmt.setInt(2, studentId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student grade updated successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }

 // ... (previous code remains unchanged)

    public static void deleteStudent() throws SQLException {
        System.out.print("Enter student ID to delete: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM students WHERE id = ?")) {
            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }
}
