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

public class Anggota extends Pengguna {
    protected List<String> keahlian;
    protected int totalTugasSelesai;
    public Anggota(String username, String email, String password, int idPengguna) {
        super(username, email, password); 
        this.idPengguna = idPengguna;   
        this.totalTugasSelesai = 0;
    }

    public void tambahAnggota(Pengguna anggota) {
        System.out.println("Anggota baru telah ditambahkan: " + anggota.getUsername());
    }

    public void buatLaporan(Proyek proyek) {
        if (proyek != null) {
            System.out.println("Laporan proyek \"" + proyek.getNamaProyek() + "\" oleh anggota \"" + this.getUsername() + "\".");
        } else {
            System.out.println("Proyek tidak valid!");
        }
    }


    public List<String> getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(List<String> keahlian) {
        this.keahlian = keahlian;
    }

    public int getTotalTugasSelesai() {
        return totalTugasSelesai;
    }

    public void setTotalTugasSelesai(int totalTugasSelesai) {
        this.totalTugasSelesai = totalTugasSelesai;
    }



    @Override
    public String toString() {
        return "Member{" +
                ", keahlian=" + keahlian +
                ", totalTugasSelesai=" + totalTugasSelesai +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
