package view;

import JDBC.DatabaseHelper;
import controller.ProyekController;
import java.util.ArrayList;
import java.util.List;
import model.Anggota;
import model.Proyek;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;

public class EditProyek extends View {
    private static Font poppinsFont;
    private Proyek proyek;
    private String idProyek; 

    public EditProyek(String idProyek) {
        this.idProyek = idProyek;
        DatabaseHelper DBHelper = new DatabaseHelper();
        proyek = DBHelper.loadProyek(idProyek); 
        initComponents();
    }

    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");

        JFrame frame = new JFrame("Edit Proyek");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Blue Gradient Panel
        JPanel bluePanel = new GradientPanel(new Color(41, 47, 172), new Color(49, 53, 255));
        bluePanel.setBounds(0, 0, frame.getWidth(), 50);
        frame.add(bluePanel);

        JLabel titleLabel = new JLabel("Edit Proyek", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 18));
        bluePanel.add(titleLabel, BorderLayout.CENTER);

        // Nama Proyek Label and TextField
        JLabel namaLabel = new JLabel("Nama Proyek:");
        namaLabel.setBounds(50, 70, 150, 25);
        namaLabel.setFont(poppinsFont);
        frame.add(namaLabel);

        JTextField namaField = new JTextField();
        namaField.setText(proyek.getNamaProyek());
        namaField.setBounds(50, 100, frame.getWidth() - 100, 30);
        namaField.setFont(poppinsFont);
        frame.add(namaField);

        // Deskripsi Proyek Label and TextField
        JLabel descLabel = new JLabel("Deskripsi Proyek:");
        descLabel.setBounds(50, 150, 150, 25);
        descLabel.setFont(poppinsFont);
        frame.add(descLabel);

        JTextField descField = new JTextField();
        descField.setText(proyek.getDeskripsi());
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

        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        loadUsers(anggotaPanel, checkBoxes, proyek.getAnggota(), new DatabaseHelper().getAllAnggota());

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
            String namaProyek = namaField.getText().trim();
            String deskripsiProyek = descField.getText().trim();

            List<Anggota> anggotaTerpilih = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    int idPengguna = Integer.parseInt(checkBox.getActionCommand());
                    anggotaTerpilih.add(new Anggota(checkBox.getText(), "", "", idPengguna));
                }
            }

            boolean success = ProyekController.updateProyek(idProyek, namaProyek, deskripsiProyek, anggotaTerpilih);
            if (success) {
                frame.dispose();

                halamanproyek instance = halamanproyek.getInstance();
                if (instance != null) {
                    instance.loadProyekList(instance.getProyekListPanel());
                    instance.setVisible(true);
                } else {
                    new halamanproyek();
                }
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
                cancelButton.setBounds(frame.getWidth() - 260, 500, 100, 30);
                saveButton.setBounds(frame.getWidth() - 140, 500, 100, 30);

                titleLabel.setFont(poppinsFont.deriveFont(18f));
            }
        });

        frame.setVisible(true);
    }

    private void loadFont(String fontPath) {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/" + fontPath);
            if (fontStream != null) {
                poppinsFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(poppinsFont);
            } else {
                poppinsFont = new Font("Poppins", Font.PLAIN, 14);
            }
        } catch (Exception e) {
            poppinsFont = new Font("Poppins", Font.PLAIN, 14);
        }
    }

    private void loadUsers(JPanel anggotaPanel, ArrayList<JCheckBox> checkBoxes, List<Anggota> anggotaProyek, List<Anggota> allAnggota) {
        for (Anggota anggota : allAnggota) {
            boolean isSelected = anggotaProyek.stream().anyMatch(a -> a.getIdPengguna() == anggota.getIdPengguna());
            JCheckBox checkBox = new JCheckBox(anggota.getUsername());
            checkBox.setFont(poppinsFont.deriveFont(Font.PLAIN, 14));
            checkBox.setBackground(Color.WHITE);
            checkBox.setSelected(isSelected);
            checkBox.setActionCommand(String.valueOf(anggota.getIdPengguna()));
            checkBoxes.add(checkBox);
            anggotaPanel.add(checkBox);
        }
    }
}
