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
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class KanbanBoard {
    private List<Tugas> toDoList;       // Kolom "To Do"
    private List<Tugas> inProgressList; // Kolom "In Progress"
    private List<Tugas> doneList;       // Kolom "Done"

    public KanbanBoard() {
        this.toDoList = new ArrayList<>();
        this.inProgressList = new ArrayList<>();
        this.doneList = new ArrayList<>();
    }

    // Menambahkan tugas ke kolom tertentu
    public void tambahTugas(Tugas tugas, String status) {
        switch (status) {
            case "To Do":
                toDoList.add(tugas);
                break;
            case "In Progress":
                inProgressList.add(tugas);
                break;
            case "Done":
                doneList.add(tugas);
                break;
            default:
                System.out.println("Status tidak valid.");
        }
    }

    // Memindahkan tugas ke kolom lain
    public void pindahTugas(String idTugas, String statusBaru) {
        Tugas tugasDipindahkan = null;

        // Cari dan hapus tugas dari kolom saat ini
        if (hapusTugasDariKolom(idTugas, toDoList)) {
            tugasDipindahkan = getTugasById(idTugas, toDoList);
        } else if (hapusTugasDariKolom(idTugas, inProgressList)) {
            tugasDipindahkan = getTugasById(idTugas, inProgressList);
        } else if (hapusTugasDariKolom(idTugas, doneList)) {
            tugasDipindahkan = getTugasById(idTugas, doneList);
        }

        // Tambahkan ke kolom baru jika ditemukan
        if (tugasDipindahkan != null) {
            tambahTugas(tugasDipindahkan, statusBaru);
        } else {
            System.out.println("Tugas dengan ID " + idTugas + " tidak ditemukan.");
        }
    }

    // Helper method untuk menghapus tugas dari kolom
    private boolean hapusTugasDariKolom(String idTugas, List<Tugas> kolom) {
        return kolom.removeIf(tugas -> tugas.getIdTugas().equals(idTugas));
    }

    // Helper method untuk mencari tugas berdasarkan ID
    private Tugas getTugasById(String idTugas, List<Tugas> kolom) {
        for (Tugas tugas : kolom) {
            if (tugas.getIdTugas().equals(idTugas)) {
                return tugas;
            }
        }
        return null;
    }

    // Menampilkan Kanban Board
    public void displayBoard() {
        System.out.println("To-Do:");
        toDoList.forEach(tugas -> System.out.println("- " + tugas.getNamaTugas()));

        System.out.println("\nIn Progress:");
        inProgressList.forEach(tugas -> System.out.println("- " + tugas.getNamaTugas()));

        System.out.println("\nDone:");
        doneList.forEach(tugas -> System.out.println("- " + tugas.getNamaTugas()));
    }
}
