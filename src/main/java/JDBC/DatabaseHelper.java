package JDBC;

import JDBC.sqlconnection;
import static JDBC.sqlconnection.connectdb;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import model.Anggota;
import model.Komentar;
import model.Pengguna;
import model.Proyek;
import model.Tugas;
import model.Tugas.Status;

public class DatabaseHelper {

    public List<Anggota> getAllAnggota() {
            List<Anggota> listAnggota = new ArrayList<>();
            String query = "SELECT id, username, email, password FROM pengguna";

            try (Connection conn = sqlconnection.connectdb();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)){
                 ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    Anggota anggota = new Anggota(username, email, password,id);
                    anggota.setIdPengguna(id);
                    listAnggota.add(anggota);
                }

            } catch (SQLException e) {
                System.err.println("Error while fetching users: " + e.getMessage());
            }

            return listAnggota;
    }

    public List<Proyek> getProyekByPengguna(int userId) {
        List<Proyek> proyekList = new ArrayList<>();
        String query = "SELECT p.id_proyek, p.nama_proyek, p.deskripsi " +
                       "FROM proyek p " +
                       "JOIN project_team_members ptm ON p.id_proyek = ptm.id_proyek " +
                       "WHERE ptm.id_pengguna = ?";

        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String idProyek = resultSet.getString("id_proyek");
                String namaProyek = resultSet.getString("nama_proyek");
                String deskripsi = resultSet.getString("deskripsi");
                List<Anggota> anggotaList = getAnggotaByProyek(idProyek);
                Proyek proyek = new Proyek(idProyek, namaProyek, deskripsi, anggotaList);
                proyekList.add(proyek);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proyekList;
    }
    public Proyek loadProyek(String idProyek) {
    String query = "SELECT * FROM proyek WHERE id_proyek = ?";
    try (Connection conn = sqlconnection.connectdb();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, idProyek);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            List<Anggota> anggotaList = getAnggotaByProyek(idProyek);
            return new Proyek(
                rs.getString("id_proyek"),
                rs.getString("nama_proyek"),
                rs.getString("deskripsi"),
                anggotaList
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    public List<Anggota> getAnggotaByProyek(String idProyek) {
        List<Anggota> anggotaList = new ArrayList<>();
        String query = "SELECT a.id, a.username FROM pengguna a " +
                       "JOIN project_team_members ptm ON a.id = ptm.id_pengguna " +
                       "WHERE ptm.id_proyek = ?";

        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, idProyek);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idAnggota = resultSet.getInt("id");
                String username = resultSet.getString("username");
                Anggota anggota = new Anggota(username, null, null, idAnggota);
                anggotaList.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return anggotaList;
    }
    
    public Proyek getProyekById(String idProyek) {
        Proyek proyek = null;
        String query = "SELECT p.id_proyek, p.nama_proyek, p.deskripsi " +
                       "FROM proyek p " +
                       "WHERE p.id_proyek = ?";

        try (Connection conn = connectdb();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, idProyek);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String namaProyek = resultSet.getString("nama_proyek");
                String deskripsi = resultSet.getString("deskripsi");

                List<Anggota> anggotaList = getAnggotaByProyek(idProyek);
                proyek = new Proyek(idProyek, namaProyek, deskripsi, anggotaList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proyek;
    }
    public Anggota findAnggotaByUsername(String username, List<Anggota> anggotaList) {
    for (Anggota anggota : anggotaList) {
        if (anggota.getUsername().equals(username)) {
            return anggota;
        }
    }
    return null; 
}
    public List<Tugas> getTugasByProyek(String idProyek) {
    List<Tugas> tugasList = new ArrayList<>();
    String query = "SELECT id_tugas, nama_tugas, deskripsi, deadline, status, prioritas FROM tugas WHERE id_proyek = ?";

    try (Connection conn = sqlconnection.connectdb();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setString(1, idProyek);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String idTugas = resultSet.getString("id_tugas");
            String namaTugas = resultSet.getString("nama_tugas");
            String deskripsi = resultSet.getString("deskripsi");
            Date deadline = resultSet.getDate("deadline");
            String statusString = resultSet.getString("status");
            String prioritas = resultSet.getString("prioritas");

            Tugas.Status status = Tugas.Status.valueOf(statusString);

            Tugas tugas = new Tugas(idTugas, namaTugas, deskripsi, deadline, status, prioritas);
            tugasList.add(tugas);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tugasList;
    }
    public void deleteTugas(String idTugas) {
        String query = "DELETE FROM tugas WHERE id_tugas = ?";
        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, idTugas);
            preparedStatement.executeUpdate();
            System.out.println("Tugas berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Tugas> getTugasByStatus(String idProyek, String status) {
        List<Tugas> tugasList = new ArrayList<>();

        String query = "SELECT * FROM tugas WHERE id_proyek = ? AND status = ?";
        try (Connection connection = sqlconnection.connectdb();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, idProyek);
            statement.setString(2, status);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String idTugas = resultSet.getString("id_tugas");
                String namaTugas = resultSet.getString("nama_tugas");
                String deskripsi = resultSet.getString("deskripsi");
                Date deadline = resultSet.getDate("deadline");  // Assuming deadline is of type Date in DB
                Status tugasStatus = Status.valueOf(resultSet.getString("status"));  // Convert string to Status enum
                String prioritas = resultSet.getString("prioritas");

                Tugas tugas = new Tugas(idTugas, namaTugas, deskripsi, deadline, tugasStatus, prioritas);
                tugasList.add(tugas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tugasList;
    }
    public Tugas getTugasById(String idTugas) {
        Tugas tugas = null;
        String query = "SELECT * FROM Tugas WHERE id_tugas = ?";

        try (Connection conn = sqlconnection.connectdb();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, idTugas);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String namaTugas = resultSet.getString("nama_tugas");
                String deskripsi = resultSet.getString("deskripsi");
                Date deadline = resultSet.getDate("deadline");
                String statusString = resultSet.getString("status");
                Status status = Status.valueOf(statusString); // Enum Status
                String prioritas = resultSet.getString("prioritas");
                tugas = new Tugas(idTugas, namaTugas, deskripsi, deadline, status, prioritas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tugas;
    }
    
    public static List<Komentar> getKomentarByTugas(String idTugas) {
        List<Komentar> komentarList = new ArrayList<>();
        try (Connection conn = sqlconnection.connectdb()) {
            String query = "SELECT k.id_komentar, k.teks, k.timestamp, p.username " +
                           "FROM komentar k " +
                           "JOIN pengguna p ON k.id_pengguna = p.id " +
                           "WHERE k.id_tugas = ? ORDER BY k.timestamp ASC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idTugas);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String idKomentar = rs.getString("id_komentar");
                String teks = rs.getString("teks");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                String username = rs.getString("username");

                Pengguna pengirim = new Pengguna(username, "", ""); 
                Komentar komentar = new Komentar(idKomentar, teks, pengirim, new Date(timestamp.getTime()), idTugas);
                komentarList.add(komentar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return komentarList;
    }
        public List<Pengguna> getAllUsers() {
            List<Pengguna> users = new ArrayList<>();
            String query = "SELECT id, username, email, password FROM pengguna";

            try (Connection conn = sqlconnection.connectdb();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)){
                 ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");

                    Pengguna user = new Pengguna(username, email, password);
                    user.setIdPengguna(id);
                    users.add(user);
                }

            } catch (SQLException e) {
                System.err.println("Error while fetching users: " + e.getMessage());
            }

            return users;
    }
        public List<Anggota> getAnggotaByTugas(String idTugas) {
            List<Anggota> anggotaList = new ArrayList<>();
            String query = "SELECT p.id, p.username " +
                           "FROM pengguna p " +
                           "JOIN anggota_tugas at ON p.id = at.id_pengguna " +
                           "WHERE at.id_tugas = ?";

            try (Connection conn = sqlconnection.connectdb();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {

                preparedStatement.setString(1, idTugas);  // Set ID tugas
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int idAnggota = resultSet.getInt("id");
                    String username = resultSet.getString("username");

                    // Membuat objek Anggota dan menambahkannya ke daftar
                    Anggota anggota = new Anggota(username, null, null, idAnggota);
                    anggotaList.add(anggota);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return anggotaList;
}

        



}


