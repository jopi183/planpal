package view;

import JDBC.DatabaseHelper;
import controller.ProyekController;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Anggota;
import model.Proyek;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class AddProyek extends View {
    private static Font poppinsFont;

    public AddProyek() {
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        ProyekController controller = new ProyekController(); 
        DatabaseHelper DBHelper = new DatabaseHelper();
        loadFont("fonts/Poppins-Regular.ttf");

        JFrame frame = new JFrame("Page Pembuatan Proyek Baru");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Blue Gradient Panel
        JPanel bluePanel = new GradientPanel(new Color(41, 47, 172), new Color(49, 53, 255));
        bluePanel.setBounds(0, 0, frame.getWidth(), 50);
        frame.add(bluePanel);

        JLabel titleLabel = new JLabel("Page Pembuatan Proyek Baru", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 18));
        bluePanel.add(titleLabel, BorderLayout.CENTER);

        // Nama Proyek Label and TextField
        JLabel namaLabel = new JLabel("Nama Proyek:");
        namaLabel.setBounds(50, 70, 150, 25);
        namaLabel.setFont(poppinsFont);
        frame.add(namaLabel);

        JTextField namaField = new JTextField();
        namaField.setBounds(50, 100, frame.getWidth() - 100, 30);
        namaField.setFont(poppinsFont);
        frame.add(namaField);

        // Deskripsi Proyek Label and TextField
        JLabel descLabel = new JLabel("Deskripsi Proyek:");
        descLabel.setBounds(50, 150, 150, 25);
        descLabel.setFont(poppinsFont);
        frame.add(descLabel);

        JTextField descField = new JTextField();
        descField.setBounds(50, 180, frame.getWidth() - 100, 30);
        descField.setFont(poppinsFont);
        frame.add(descField);

        // Anggota Tim Label
        JLabel anggotaLabel = new JLabel("Anggota Tim:");
        anggotaLabel.setBounds(50, 230, 150, 25);
        anggotaLabel.setFont(poppinsFont);
        frame.add(anggotaLabel);

        // Scrollable Panel for Team Members
        JPanel anggotaPanel = new JPanel();
        anggotaPanel.setLayout(new BoxLayout(anggotaPanel, BoxLayout.Y_AXIS));
        anggotaPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(anggotaPanel);
        scrollPane.setBounds(50, 260, frame.getWidth() - 100, 100);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);
        
        List<Anggota> anggotaList = DBHelper.getAllAnggota();
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (Anggota anggota : anggotaList) {
            System.out.println("Anggota: " + anggota.getUsername() + " dengan ID: " + anggota.getIdPengguna());
            System.out.println("ID Anggota: " + anggota.getIdPengguna());
            JCheckBox checkBox = new JCheckBox(anggota.getUsername());
            checkBox.setActionCommand(String.valueOf(anggota.getIdPengguna()));
            checkBoxes.add(checkBox);
            anggotaPanel.add(checkBox);
        }

        // Tambah Anggota TextField with Placeholder
        JTextField tambahAnggotaField = new JTextField();
        tambahAnggotaField.setBounds(50, 370, frame.getWidth() - 200, 30);
        tambahAnggotaField.setFont(poppinsFont);
        addPlaceholder(tambahAnggotaField, "Tambah anggota lain...");
        frame.add(tambahAnggotaField);

        // Tambah Button
        JButton tambahButton = new JButton("Tambah");
        tambahButton.setBounds(frame.getWidth() - 140, 370, 100, 30);
        tambahButton.setFont(poppinsFont);
        tambahButton.setBackground(new Color(44, 49, 197));
        tambahButton.setForeground(Color.WHITE);
        frame.add(tambahButton);
        
        tambahButton.addActionListener(e -> {
            String anggotaBaru = tambahAnggotaField.getText().trim();
            if (!anggotaBaru.isEmpty() && !anggotaBaru.equals("Tambah anggota lain...")) {
                int idFiktif = -1;  
                addCheckBox(anggotaBaru, anggotaPanel, checkBoxes, idFiktif);  
                tambahAnggotaField.setText("");  
                anggotaPanel.revalidate();  
                anggotaPanel.repaint();  
            } else {
                JOptionPane.showMessageDialog(frame, "Nama anggota tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(frame.getWidth() - 260, 500, 100, 30);
        cancelButton.setFont(poppinsFont);
        cancelButton.setBackground(new Color(44, 49, 197));
        cancelButton.setForeground(Color.WHITE);
        frame.add(cancelButton);
        
        cancelButton.addActionListener(e -> {
            frame.dispose();
            new halamanproyek();
        });

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(frame.getWidth() - 140, 500, 100, 30);
        saveButton.setFont(poppinsFont);
        saveButton.setBackground(new Color(44, 49, 197));
        saveButton.setForeground(Color.WHITE);
        frame.add(saveButton);
        
        saveButton.addActionListener(e -> {
            String idProyek = UUID.randomUUID().toString().substring(0, 8);
            String namaProyek = namaField.getText().trim();
            String deskripsi = descField.getText().trim();
            List<Integer> idPenggunaTerpilih = new ArrayList<>();

            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    System.out.println("ID Pengguna yang dipilih: " + checkBox.getActionCommand());
                    idPenggunaTerpilih.add(Integer.parseInt(checkBox.getActionCommand()));
                }
            }
            Proyek proyekBaru = controller.buatProyekBaru(idProyek, namaProyek, deskripsi, idPenggunaTerpilih);

            if (proyekBaru != null) {
                JOptionPane.showMessageDialog(frame, "Proyek berhasil dibuat!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new halamanproyek();
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal membuat proyek. Periksa input Anda!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add resize listener for responsiveness
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bluePanel.setBounds(0, 0, frame.getWidth(), 50);
                namaField.setBounds(50, 100, frame.getWidth() - 100, 30);
                descField.setBounds(50, 180, frame.getWidth() - 100, 30);
                scrollPane.setBounds(50, 260, frame.getWidth() - 100, 100);
                tambahAnggotaField.setBounds(50, 370, frame.getWidth() - 200, 30);
                tambahButton.setBounds(frame.getWidth() - 140, 370, 100, 30);
                cancelButton.setBounds(frame.getWidth() - 260, 500, 100, 30);
                saveButton.setBounds(frame.getWidth() - 140, 500, 100, 30);

                titleLabel.setFont(poppinsFont.deriveFont(18f)); // Fixed font size
            }
        });
        
        frame.setVisible(true);
    }

    private void loadFont(String fontPath) {
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if (fontStream != null) {
                poppinsFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(poppinsFont);
            } else {
                poppinsFont = new Font("Arial", Font.PLAIN, 14);
            }
        } catch (Exception e) {
            poppinsFont = new Font("Arial", Font.PLAIN, 14);
        }
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    private void addCheckBox(String text, JPanel panel, ArrayList<JCheckBox> checkBoxes, int idPengguna) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(poppinsFont.deriveFont(Font.PLAIN, 14));
        checkBox.setBackground(Color.WHITE);
        checkBox.setActionCommand(String.valueOf(idPengguna));  
        checkBoxes.add(checkBox);
        panel.add(checkBox);
    }
}
