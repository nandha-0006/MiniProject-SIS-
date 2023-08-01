package com.teacher;

import java.sql.*;
import java.util.Scanner;

public class Teacher {
    public static String DB_URL = "jdbc:mysql://localhost:3306/mini?user=root&password=tiger";
    private static Scanner scanner = new Scanner(System.in);
    public Teacher(String dbUrl) {
        DB_URL = dbUrl;
    }
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (teacherLogin()) {
                while (true) {
                    displayTeacherMenu();
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            addStudent();
                            break;
                        case 2:
                            displayStudents();
                            break;
                        case 3:
                            updateStudentGrade();
                            break;
                        case 4:
                            System.out.println("Exiting the program.");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else {
                System.out.println("Teacher login failed. Exiting the program.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean teacherLogin() {
        System.out.println("Teacher Login:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            return TeacherLoginTable.isTeacherLoginValid(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ... Rest of the methods remain the same as before ...


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

    public static void displayTeacherMenu() {
        System.out.println("\nTeacher Menu:");
        System.out.println("1. Add Student");
        System.out.println("2. Display Students");
        System.out.println("3. Update Student Grade");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }
}
