package config;

import java.sql.*;
import java.util.*;

public class userTables {
    private Connection conn;

    
    public userTables() {
        connectDB();
        createTables();
    }

    
    private void connectDB() {
        try {
            // Example using SQLite. Replace with your DB URL
            String url = "jdbc:sqlite:vetmanagement.db";
            conn = DriverManager.getConnection(url);
            System.out.println("✅ Database Connected (userTables)");
        } catch (SQLException e) {
            System.out.println("❌ Connection Failed: " + e.getMessage());
        }
    }

    // Create missing tables if they don't exist
    private void createTables() {
        String tblAdmin = "CREATE TABLE IF NOT EXISTS tbl_admin (" +
                          "admin_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "name TEXT NOT NULL," +
                          "email TEXT UNIQUE NOT NULL," +
                          "password TEXT NOT NULL," +
                          "type TEXT NOT NULL," +
                          "status TEXT NOT NULL" +
                          ");";

        String tblOwner = "CREATE TABLE IF NOT EXISTS tbl_owner (" +
                          "owner_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "name TEXT NOT NULL," +
                          "email TEXT UNIQUE NOT NULL," +
                          "password TEXT NOT NULL," +
                          "status TEXT NOT NULL" +
                          ");";

        String tblStaff = "CREATE TABLE IF NOT EXISTS tbl_staff (" +
                          "staff_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "owner_id INTEGER," +
                          "name TEXT NOT NULL," +
                          "email TEXT UNIQUE NOT NULL," +
                          "password TEXT NOT NULL," +
                          "status TEXT NOT NULL," +
                          "FOREIGN KEY(owner_id) REFERENCES tbl_owner(owner_id)" +
                          ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(tblAdmin);
            stmt.execute(tblOwner);
            stmt.execute(tblStaff);
            System.out.println("✅ Tables (Admin, Owner, Staff) Ready");
        } catch (SQLException e) {
            System.out.println("❌ Table Creation Failed: " + e.getMessage());
        }
    }

    // Optional: fetchRecords method like your config class
    public List<Map<String, Object>> fetchRecords(String query, String... params) {
        List<Map<String, Object>> records = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pst.setString(i + 1, params[i]);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                records.add(row);
            }
        } catch (SQLException e) {
            System.out.println("❌ Fetch Failed: " + e.getMessage());
        }
        return records;
    }

    // Close connection
    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {}
    }
}
