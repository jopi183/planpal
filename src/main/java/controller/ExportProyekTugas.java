/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ExportProyekTugas {

    private static final String DB_URL = "jdbc:mysql://localhost:3307/db_planpal";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        String userId = "1"; 
        exportDataToExcel(userId);
    }

    public static void exportDataToExcel(String userId) {
        String downloadsDir = System.getProperty("user.home") + "/Downloads";
        String filePath = downloadsDir + "/Daftar_Proyek_Tugas.xlsx";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Workbook workbook = new XSSFWorkbook()) {

            String sql = "SELECT p.nama_proyek, t.nama_tugas, t.deskripsi, t.deadline, t.status " +
                         "FROM proyek p " +
                         "JOIN tugas t ON p.id_proyek = t.id_proyek " +
                         "JOIN anggota_tugas at ON t.id_tugas = at.id_tugas " +
                         "WHERE at.id_pengguna = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();

            Sheet sheet = workbook.createSheet("Proyek dan Tugas");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nama Proyek", "Nama Tugas", "Deskripsi", "Deadline", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getString("nama_proyek"));
                row.createCell(1).setCellValue(rs.getString("nama_tugas"));
                row.createCell(2).setCellValue(rs.getString("deskripsi"));
                row.createCell(3).setCellValue(rs.getDate("deadline").toString());
                row.createCell(4).setCellValue(rs.getString("status"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            System.out.println("File Excel berhasil dibuat di folder Downloads: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
