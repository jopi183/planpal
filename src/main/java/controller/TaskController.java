/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import JDBC.sqlconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.Anggota;
import model.Tugas;

/**
 *
 * @author umark
 */
public class TaskController {
    private Connection conn;

    public TaskController() {
        this.conn = sqlconnection.connectdb(); 
    }
    
    public boolean saveTaskToDatabase(String namaTugas, String deskripsi, Date deadline, String prioritas, ArrayList<Integer> selectedUserIds, String idProyek) {
        try (Connection conn = sqlconnection.connectdb()) {
            conn.setAutoCommit(false); 

            String idTugas = generateTaskId();

            String queryTugas = "INSERT INTO tugas (id_tugas, id_proyek, nama_tugas, deskripsi, deadline, prioritas, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtTugas = conn.prepareStatement(queryTugas)) {
                stmtTugas.setString(1, idTugas);
                stmtTugas.setString(2, idProyek); 
                stmtTugas.setString(3, namaTugas);
                stmtTugas.setString(4, deskripsi);
                stmtTugas.setTimestamp(5, new Timestamp(deadline.getTime()));
                stmtTugas.setString(6, prioritas);
                stmtTugas.setString(7, "TODO"); 
                stmtTugas.executeUpdate();
            }
            String queryAnggota = "INSERT INTO anggota_tugas (id_tugas, id_pengguna) VALUES (?, ?)";
            try (PreparedStatement stmtAnggota = conn.prepareStatement(queryAnggota)) {
                for (int userId : selectedUserIds) {
                    stmtAnggota.setString(1, idTugas);
                    stmtAnggota.setInt(2, userId);
                    stmtAnggota.addBatch();
                }
                stmtAnggota.executeBatch();
            }

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   

        public String generateTaskId() {
            return "TGS" + System.currentTimeMillis(); 
        }

    private boolean assignUserToTask(String idTugas, int idPengguna) {
        try {
            String query = "INSERT INTO anggota_tugas (id_tugas, id_pengguna) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, idTugas);
                stmt.setInt(2, idPengguna);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan anggota tugas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateTask(String idTugas, String namaTugas, String deskripsi, Timestamp deadline,
                                 String prioritas, List<Anggota> anggotaTim) {
    Connection conn = sqlconnection.connectdb();
    if (conn == null) {
        return false; // Tidak perlu JOptionPane, cukup kembalikan false
    }

    try {
        // Update task details
        String updateTaskQuery = "UPDATE tugas SET nama_tugas = ?, deskripsi = ?, deadline = ?, prioritas = ? WHERE id_tugas = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateTaskQuery)) {
            pstmt.setString(1, namaTugas);
            pstmt.setString(2, deskripsi);
            pstmt.setTimestamp(3, deadline);
            pstmt.setString(4, prioritas);
            pstmt.setString(5, idTugas);
            pstmt.executeUpdate();
        }

        // Remove existing task members
        String deleteAnggotaTimQuery = "DELETE FROM anggota_tugas WHERE id_tugas = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteAnggotaTimQuery)) {
            pstmt.setString(1, idTugas);
            pstmt.executeUpdate();
        }

        // Add new task members
        String insertAnggotaQuery = "INSERT INTO anggota_tugas (id_tugas, id_pengguna) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertAnggotaQuery)) {
            for (Anggota anggota : anggotaTim) {
                int idPengguna = anggota.getIdPengguna();

                // Validate id_pengguna exists in pengguna table
                if (!isPenggunaValid(conn, idPengguna)) {
                    return false; // Tidak valid, kembalikan false
                }

                pstmt.setString(1, idTugas);
                pstmt.setInt(2, idPengguna);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        return true; // Jika semua sukses

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        sqlconnection.disconnect();
    }
}

    private static boolean isPenggunaValid(Connection conn, int idPengguna) throws SQLException {
        String query = "SELECT COUNT(*) FROM pengguna WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idPengguna);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    public static boolean deleteTaskById(String idTugas) {
            try (Connection conn = sqlconnection.connectdb()) {
                // Query untuk menghapus tugas berdasarkan id_tugas
                String query = "DELETE FROM tugas WHERE id_tugas = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, idTugas);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Tugas berhasil dihapus.",
                            "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Tugas tidak ditemukan atau sudah dihapus.",
                            "Kesalahan",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Terjadi kesalahan saat menghapus tugas: " + e.getMessage(),
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return false;
            }
        }
    
    public static void updateStatusTask(String idTugas, Tugas.Status status) {
        String query = "UPDATE tugas SET status = ? WHERE id_tugas = ?";

        try (Connection conn = sqlconnection.connectdb(); 
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, status.name()); 
            preparedStatement.setString(2, idTugas); 
            preparedStatement.executeUpdate();
            System.out.println("Status tugas berhasil diperbarui.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
