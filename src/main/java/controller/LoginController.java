/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.List;
import model.Pengguna;

/**
 *
 * @author ASUS
 */
public class LoginController {
    private final List<Pengguna> users;
    private String username;
    private String password;

    public LoginController(List<Pengguna> users, String username, String password) {
        this.users = users;
        this.username = username;
        this.password = password;
    }

    public boolean authenticate(String username, String password) {
        for (Pengguna user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public Pengguna getLoggedInUser(String username, String password) {
        for (Pengguna user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

}