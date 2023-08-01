package com.teacher;


import java.sql.*;

public class TeacherLoginTable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mini?user=root&password=tiger";
    private static final String ADMIN_TABLE_NAME = "tlogin";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver.
            createAdminTableIfNotExists();

            // Sample code to add an admin (You can modify this as needed)
            addTeacher("nava1", "123");

            // Sample code to check admin login (You can modify this as needed)
            if (isTeacherLoginValid("nava", "123")) {
                System.out.println("Teacher login successful.");
            } else {
                System.out.println("Invalid teacher credentials.");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createAdminTableIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS " + ADMIN_TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(50) NOT NULL)";
            stmt.execute(sql);
        }
    }

    private static void addTeacher(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + ADMIN_TABLE_NAME + " (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        }
    }

    public static boolean isTeacherLoginValid(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + ADMIN_TABLE_NAME + " WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}