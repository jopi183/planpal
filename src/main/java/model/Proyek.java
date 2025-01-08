/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
import java.util.List;


public class Proyek {
    private String idProyek;
    private String namaProyek;
    private String deskripsi;
    private KanbanBoard kanBanBoard;
    private List<Anggota> anggota;  // Anggota proyek
    private List<Tugas> tasks;

    public Proyek(String idProyek, String namaProyek, String deskripsi, List<Anggota> anggota) {
        this.idProyek = idProyek;
        this.namaProyek = namaProyek;
        this.deskripsi = deskripsi;
        this.anggota = anggota;
    }

    public String getIdProyek() {
        return idProyek;
    }

    public void setIdProyek(String idProyek) {
        this.idProyek = idProyek;
    }

    public String getNamaProyek() {
        return namaProyek;
    }

    public void setNamaProyek(String namaProyek) {
        this.namaProyek = namaProyek;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public KanbanBoard getKanBanBoard() {
        return kanBanBoard;
    }

    public List<Anggota> getAnggota() {
        return anggota;
    }

    public void setAnggota(List<Anggota> anggota) {
        this.anggota = anggota;
    }

    public void lihatProgres() {
        System.out.println("Progres Proyek: " + namaProyek + " (ID: " + idProyek + ")");
        kanBanBoard.displayBoard();
    }

    public void tambahAnggota(Anggota anggota) {
        if (!this.anggota.contains(anggota)) {
            this.anggota.add(anggota);
            System.out.println("Anggota \"" + anggota.getUsername() + "\" berhasil ditambahkan ke proyek.");
        } else {
            System.out.println("Anggota sudah ada dalam proyek.");
        }
    }
}
