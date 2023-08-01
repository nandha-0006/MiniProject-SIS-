package com.student;


import java.sql.*;

public class StudentLoginTable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mini?user=root&password=tiger";
    private static final String STUDENT_LOGIN_TABLE_NAME = "students_login";

    public StudentLoginTable(String dB_URL2) {
		// TODO Auto-generated constructor stub
	}



	public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver.
            createStudentLoginTableIfNotExists();

            // Sample code to add a student login (You can modify this as needed)
            addStudentLogin("student2", "password123");

            // Sample code to check student login (You can modify this as needed)
            if (isStudentLoginValid("student1", "password123")) {
                System.out.println("Student login successful.");
            } else {
                System.out.println("Invalid student credentials.");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createStudentLoginTableIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + STUDENT_LOGIN_TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(50) NOT NULL)";
            stmt.execute(sql);
        }
    }

    private static void addStudentLogin(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + STUDENT_LOGIN_TABLE_NAME + " (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
    }

    public static boolean isStudentLoginValid(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + STUDENT_LOGIN_TABLE_NAME + " WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
