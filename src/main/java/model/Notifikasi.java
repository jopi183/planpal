/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public interface Notifikasi {
    void kirimPesan(String pesan, Pengguna pengguna);
    String getPesan();
}