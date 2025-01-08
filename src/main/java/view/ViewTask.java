package view;

import JDBC.DatabaseHelper;
import auth.SessionManager;
import controller.AddKomentarController;
import model.Komentar;
import model.Pengguna;
import model.Tugas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.border.TitledBorder;

public class ViewTask extends View {
    private Font poppinsFont;
    private final Tugas tugas;
    private JTextArea komentarArea;

    public ViewTask(Tugas tugas) {
        this.tugas = tugas;
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");
        JFrame frame = new JFrame("Detail Tugas");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Color primaryColor = new Color(45, 60, 190);
        Color secondaryColor = new Color(230, 230, 255);
        Color buttonColor = new Color(45, 60, 190);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(primaryColor);
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Detail Tugas");
        headerLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(3, 1, 10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(secondaryColor);

        JLabel namaLabel = new JLabel("Nama Tugas:");
        namaLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        JLabel namaValue = new JLabel(tugas.getNamaTugas());
        namaValue.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        JLabel deskripsiLabel = new JLabel("Deskripsi:");
        deskripsiLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        JTextArea deskripsiValue = new JTextArea(tugas.getDeskripsi());
        deskripsiValue.setFont(poppinsFont.deriveFont(Font.PLAIN, 15));
        deskripsiValue.setLineWrap(true);
        deskripsiValue.setWrapStyleWord(true);
        deskripsiValue.setEditable(false);
        deskripsiValue.setBackground(secondaryColor);

        JLabel deadlineLabel = new JLabel("Deadline:");
        deadlineLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        JLabel deadlineValue = new JLabel(dateFormat.format(tugas.getDeadline()));
        deadlineValue.setFont(poppinsFont.deriveFont(Font.BOLD, 15));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        JLabel statusValue = new JLabel(tugas.getStatus().toString());  // Menampilkan status tugas (DO, IN_PROGRESS, DONE)
        statusValue.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        
        JLabel prioritasLabel = new JLabel("Prioritas:");
        prioritasLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        JLabel prioritasValue = new JLabel(tugas.getPrioritas());
        prioritasValue.setFont(poppinsFont.deriveFont(Font.BOLD, 15));
        

        infoPanel.add(namaLabel);
        infoPanel.add(namaValue);
        infoPanel.add(deskripsiLabel);
        infoPanel.add(new JScrollPane(deskripsiValue));
        infoPanel.add(deadlineLabel);
        infoPanel.add(deadlineValue);
        infoPanel.add(prioritasLabel);
        infoPanel.add(prioritasValue);
        infoPanel.add(statusLabel);
        infoPanel.add(statusValue);

        contentPanel.add(infoPanel);

        JPanel komentarPanel = new JPanel(new BorderLayout());
        komentarPanel.setBackground(secondaryColor);
        Font borderFont = poppinsFont.deriveFont(Font.BOLD, 15);
        TitledBorder border = BorderFactory.createTitledBorder("Komentar");
        border.setTitleFont(borderFont);
        komentarPanel.setBorder(border);

        this.komentarArea = new JTextArea();
        komentarArea.setEditable(false);
        komentarArea.setLineWrap(true);
        komentarArea.setWrapStyleWord(true);
        komentarArea.setFont(poppinsFont.deriveFont(Font.PLAIN, 15));

        // Load initial comments
        refreshKomentarArea();

        komentarPanel.add(new JScrollPane(komentarArea), BorderLayout.CENTER);
        contentPanel.add(komentarPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addKomentarButton = new JButton("Add Komentar");
        addKomentarButton.setBackground(buttonColor);
        addKomentarButton.setForeground(Color.WHITE);
        addKomentarButton.setFont(poppinsFont.deriveFont(Font.PLAIN, 16));
        addKomentarButton.setFocusPainted(false);

        addKomentarButton.addActionListener(e -> {
            Pengguna pengguna = SessionManager.getCurrentUser();
            if (pengguna == null) {
                JOptionPane.showMessageDialog(frame, "Tidak ada pengguna yang terdaftar. Silakan login terlebih dahulu.");
                return;
            }

            String komentarBaru = JOptionPane.showInputDialog(frame, "Masukkan komentar baru:");
            if (komentarBaru != null && !komentarBaru.trim().isEmpty()) {
                Komentar komentar = new Komentar(
                        "K" + System.currentTimeMillis(),
                        komentarBaru,
                        pengguna,
                        new Date(),
                        tugas.getIdTugas()
                );

                boolean success = AddKomentarController.addKomentar(komentar);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Komentar berhasil ditambahkan.");
                    tugas.getKomentarList().add(komentar);
                    refreshKomentarArea();
                } else {
                    JOptionPane.showMessageDialog(frame, "Gagal menambahkan komentar.");
                }
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(buttonColor);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(poppinsFont.deriveFont(Font.PLAIN, 16));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(addKomentarButton);
        buttonPanel.add(closeButton);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    
    private void loadFont(String fontPath) {
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null) {
                throw new Exception("Font file not found: " + fontPath);
            }

            poppinsFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsFont);

        } catch (Exception e) {
            System.err.println("Failed to load font: " + fontPath);
            e.printStackTrace();
            poppinsFont = new Font("Arial", Font.PLAIN, 16);
        }
    }
    
    private void refreshKomentarArea() {
        komentarArea.setText("");
        List<Komentar> komentarList = tugas.getKomentarList();
        for (Komentar komentar : komentarList) {
            komentarArea.append(komentar.getPengirim().getUsername() + " (" + komentar.getTimestamp() + "):\n" +
                    komentar.getTeks() + "\n\n");
        }
    }
}
