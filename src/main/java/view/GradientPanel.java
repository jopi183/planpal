/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    private final Color colorStart; // Warna atas
    private final Color colorEnd;   // Warna bawah

    public GradientPanel(Color colorStart, Color colorEnd) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Aktifkan anti-aliasing untuk hasil yang lebih halus
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Gradient dari atas (0,0) ke bawah (0, height)
        GradientPaint gradient = new GradientPaint(0, 0, colorStart, 0, getHeight(), colorEnd);
        g2d.setPaint(gradient);

        // Gambar gradient di seluruh panel
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
