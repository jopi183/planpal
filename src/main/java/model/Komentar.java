/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Komentar {
    private String idKomentar;
    private String teks;
    private Pengguna pengirim;
    private Date timestamp;
    private String tugasId;

    public Komentar(String idKomentar, String teks, Pengguna pengirim, Date timestamp, String tugasId) {
        this.idKomentar = idKomentar;
        this.teks = teks;
        this.pengirim = pengirim;
        this.timestamp = timestamp;
        this.tugasId = tugasId;
    }

    public String getIdKomentar() {
        return idKomentar;
    }

    public String getTeks() {
        return teks;
    }

    public Pengguna getPengirim() {
        return pengirim;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTugasId() {
        return tugasId;
    }
}
