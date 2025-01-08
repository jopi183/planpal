/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

/**
 *
 * @author ASUS
 */
import JDBC.sqlconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Pengguna;

public class RegisterManager {
    public boolean registerUser(Pengguna user) throws SQLException {
        String insertQuery = "INSERT INTO pengguna (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement pst = conn.prepareStatement(insertQuery)) {

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
