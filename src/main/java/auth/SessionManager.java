/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import model.Pengguna;

/**
 *
 * @author ASUS
 */
public class SessionManager {
    private static int currentUserID;
    private static Pengguna currentUser;

    public static void setCurrentUserID(int userID) {
        currentUserID = userID;
        currentUser = getUserByID(userID); 
    }
    public static void setCurrentUser(Pengguna pengguna) {
        currentUser = pengguna;
    }

    public static int getCurrentUserID() {
        return currentUserID;
    }

    public static Pengguna getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUserID = 0;
        currentUser = null;
    }

    private static Pengguna getUserByID(int userID) {
        Pengguna pengguna = null;
        return pengguna;
    }
}
