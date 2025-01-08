package controller;

import JDBC.DatabaseHelper;
import JDBC.sqlconnection;
import model.Anggota;
import model.Proyek;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyekController {
    private List<Anggota> anggotaList;
    private DatabaseHelper databaseHelper;

    public ProyekController() {
        this.databaseHelper = new DatabaseHelper();
        this.anggotaList = new ArrayList<>();
        fetchAnggotaDariDatabase();
    }

    public void fetchAnggotaDariDatabase() {
        try {
            anggotaList = databaseHelper.getAllAnggota();
            if (anggotaList.isEmpty()) {
                System.out.println("Tidak ada anggota yang tersedia di database.");
            } else {
                System.out.println("Berhasil memuat anggota dari database.");
            }
        } catch (Exception e) {
            System.err.println("Error saat memuat anggota: " + e.getMessage());
        }
    }

    public Proyek buatProyekBaru(String idProyek, String namaProyek, String deskripsi, List<Integer> idPenggunaList) {
        if (namaProyek == null || namaProyek.isEmpty()) {
            System.out.println("Nama proyek tidak boleh kosong!");
            return null;
        }

        if (idPenggunaList.isEmpty()) {
            System.out.println("Proyek harus memiliki minimal satu anggota!");
            return null;
        }

        List<Anggota> anggotaProyek = new ArrayList<>();
        for (Integer id : idPenggunaList) {
            for (Anggota anggota : anggotaList) {
                if (anggota.getIdPengguna() == id) {
                    anggotaProyek.add(anggota);
                }
            }
        }

        Proyek proyekBaru = new Proyek(idProyek, namaProyek, deskripsi, anggotaProyek);

        try {
            saveProyek(proyekBaru, idPenggunaList);
            System.out.println("Proyek \"" + namaProyek + "\" berhasil dibuat dengan ID: " + idProyek);
            return proyekBaru;
        } catch (Exception e) {
            System.err.println("Error saat menyimpan proyek: " + e.getMessage());
            return null;
        }
    }

    public void saveProyek(Proyek proyek, List<Integer> idPenggunaList) {
        String query = "INSERT INTO proyek (id_proyek, nama_proyek, deskripsi) VALUES (?, ?, ?)";

        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, proyek.getIdProyek());
            preparedStatement.setString(2, proyek.getNamaProyek());
            preparedStatement.setString(3, proyek.getDeskripsi());
            preparedStatement.executeUpdate();

            saveProyekAnggota(proyek.getIdProyek(), idPenggunaList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveProyekAnggota(String idProyek, List<Integer> idPenggunaList) {
        String query = "INSERT INTO project_team_members (id_proyek, id_pengguna) VALUES (?, ?)";

        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            for (int idPengguna : idPenggunaList) {
                preparedStatement.setString(1, idProyek);
                preparedStatement.setInt(2, idPengguna);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteProyekById(String idProyek) {
        try (Connection conn = sqlconnection.connectdb()) {
            String query = "DELETE FROM proyek WHERE id_proyek = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idProyek);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null,
                        "Proyek berhasil dihapus.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Proyek tidak ditemukan atau sudah dihapus.",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat menghapus proyek: " + e.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateProyek(String idProyek, String namaProyek, String deskripsi, List<Anggota> anggotaTim) {
        Connection conn = sqlconnection.connectdb();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            conn.setAutoCommit(false);
            String updateProyekQuery = "UPDATE proyek SET nama_proyek = ?, deskripsi = ? WHERE id_proyek = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateProyekQuery)) {
                pstmt.setString(1, namaProyek);
                pstmt.setString(2, deskripsi);
                pstmt.setString(3, idProyek);
                pstmt.executeUpdate();
            }

            String deleteAnggotaTimQuery = "DELETE FROM project_team_members WHERE id_proyek = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteAnggotaTimQuery)) {
                pstmt.setString(1, idProyek);
                pstmt.executeUpdate();
            }

            String insertAnggotaQuery = "INSERT INTO project_team_members (id_proyek, id_pengguna) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertAnggotaQuery)) {
                for (Anggota anggota : anggotaTim) {
                    int idPengguna = anggota.getIdPengguna();
                    if (!isPenggunaValid(conn, idPengguna)) {
                        JOptionPane.showMessageDialog(null,
                                "Pengguna dengan ID " + idPengguna + " tidak ditemukan!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        throw new SQLException("Foreign key constraint violation: id_pengguna not found.");
                    }

                    pstmt.setString(1, idProyek);
                    pstmt.setInt(2, idPengguna);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
            JOptionPane.showMessageDialog(null, "Proyek berhasil diperbarui!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Error updating project: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
}
