/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PomodoroTimer;

import java.io.File;
import java.sql.*;

/**
 *
 * @author Cameron B
 */
public class FocusDB {

    private Connection conn;
    String dbFilePath = "focusDB.db";

    FocusDB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e);
        }
        try {
            File dbFile = new File(dbFilePath);
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
            String createTableSQL = "CREATE TABLE IF NOT EXISTS focus_sessions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "duration_minutes INTEGER, "
                    + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.toString());
        }
    }

    public int getLoggedMinutes() {
        if (conn == null) {
            System.out.println("Database connection not established. Returning 0 logged minutes.");
            return 0;
        }
        String selectSQL = "SELECT * FROM focus_sessions";
        int durationMinutes = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                durationMinutes += rs.getInt("duration_minutes");
                String timestamp = rs.getString("timestamp");
                System.out
                        .println("ID: " + id + ", Duration: " + durationMinutes + " minutes, Timestamp: " + timestamp);
            }
            return durationMinutes;
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.toString());
        }
        return -1;
    }

    public void logSession(int durationMinutes) {
        if (conn == null) {
            System.out.println("Cannot log session: no DB connection.");
            return;
        }
        String insertSQL = "INSERT INTO focus_sessions (duration_minutes) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, durationMinutes);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.toString());
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.toString());
        }
    }

    public void clearFocusHistory() {
        if (conn == null) {
            System.out.println("Cannot clear history: no DB connection.");
            return;
        }
        String deleteSQL = "DELETE FROM focus_sessions";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL Exception: " + ex.toString());
        }
    }
}
