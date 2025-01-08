/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author ASUS
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import model.Komentar;
import JDBC.sqlconnection;

public class AddKomentarController {

    public static boolean addKomentar(Komentar komentar) {
        String sql = "INSERT INTO komentar (id_komentar, id_tugas, id_pengguna, teks, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, komentar.getIdKomentar());
            pstmt.setString(2, komentar.getTugasId());
            pstmt.setInt(3, komentar.getPengirim().getIdPengguna());
            pstmt.setString(4, komentar.getTeks());
            pstmt.setTimestamp(5, new java.sql.Timestamp(komentar.getTimestamp().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
