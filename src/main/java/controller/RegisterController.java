/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author ASUS
 */

import auth.RegisterManager;
import java.sql.SQLException;
import java.util.List;
import model.Pengguna;

public class RegisterController {
    private final RegisterManager registerManager;

    public RegisterController(RegisterManager registerManager) {
        this.registerManager = registerManager;
    }

    public String registerUser(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "All fields are required.";
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Invalid email format.";
        }
        try {
            Pengguna newUser = new Pengguna(username, email, password);
            boolean isSuccess = registerManager.registerUser(newUser);
            return isSuccess ? "Registration successful." : "Registration failed. Please try again.";
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                return "Username or email already used.";
            }
            ex.printStackTrace();
            return "An unexpected error occurred.";
        }
    }
}


