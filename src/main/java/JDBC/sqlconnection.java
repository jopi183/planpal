/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author ASUS
 */

import javax.swing.*;
import java.sql.*;

public class sqlconnection {

    static Statement stmt;
    static ResultSet rs;
    static Connection conn;  
    
    public static Connection connectdb() {
        String dbUsername = "root";
        String dbPassword = "";
        String dbUrl = "jdbc:mysql://localhost:3306/db_planpal";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            stmt = conn.createStatement();  //
            return conn;

        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return null;
    }

    public static void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();  
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            rs = stmt.executeQuery(query);  
            return rs;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return null;
    }

    public static int executeUpdate(String query) {
        try {
            int rowsAffected = stmt.executeUpdate(query);  
            return rowsAffected;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return 0;
    }
}