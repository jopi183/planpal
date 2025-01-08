/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public class NotifikasiImplementasi implements Notifikasi {
    private String pesan;

    @Override
    public void kirimPesan(String pesan, Pengguna pengguna) {
        this.pesan = "Haii!!!  " + pengguna.getUsername() + " - " + pesan;
        System.out.println("Notifikasi terkirim: " + this.pesan);
    }

    @Override
    public String getPesan() {
        return pesan;
    }

}
