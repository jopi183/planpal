/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
import JDBC.DatabaseHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tugas {
    private String idTugas;
    private String namaTugas;
    private String deskripsi;
    private Date deadline;
    private Status status; // Menggunakan enum untuk status
    private String prioritas; // Tinggi, Menengah, Rendah
    private List<Komentar> komentarList;

    // Enum untuk status
    public enum Status {
        TODO,          // Representasi kolom "To Do"
        IN_PROGRESS,   // Representasi kolom "In Progress"
        DONE           // Representasi kolom "Done"
    }

    public Tugas(String idTugas, String namaTugas, String deskripsi, Date deadline, Status status, String prioritas) {
        this.idTugas = idTugas;
        this.namaTugas = namaTugas;
        this.deskripsi = deskripsi;
        this.deadline = deadline;
        this.status = status;
        this.prioritas = prioritas;
        this.komentarList = new ArrayList<>();
    }

    public String getIdTugas() {
        return idTugas;
    }

    public void setIdTugas(String idTugas) {
        this.idTugas = idTugas;
    }

    public String getNamaTugas() {
        return namaTugas;
    }

    public void setNamaTugas(String namaTugas) {
        this.namaTugas = namaTugas;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

public List<Komentar> getKomentarList() {
    DatabaseHelper dbhelper = new DatabaseHelper();
    return dbhelper.getKomentarByTugas(this.idTugas);
}

    // Method ubahStatus
    public void ubahStatus(Status status) {
        this.status = status;
        System.out.println("Status tugas berhasil diubah menjadi: " + status);
    }

    public void tambahKomentar(Komentar komentar) {
        komentarList.add(komentar);
        System.out.println("Komentar berhasil ditambahkan: " + komentar.getTeks());
    }

    public void displayDetailTugas() {
        System.out.println("ID Tugas: " + idTugas);
        System.out.println("Nama Tugas: " + namaTugas);
        System.out.println("Deskripsi: " + deskripsi);
        System.out.println("Deadline: " + deadline);
        System.out.println("Status: " + status);
        System.out.println("Prioritas: " + prioritas);
        System.out.println("Daftar Komentar:");
        for (Komentar komentar : komentarList) {
            System.out.println("- " + komentar.getTeks() + " (oleh: " + komentar.getPengirim()+ ")");
        }
    }
    
}
