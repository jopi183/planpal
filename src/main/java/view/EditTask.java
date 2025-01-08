package view;

import JDBC.DatabaseHelper;
import controller.TaskController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Anggota;
import model.Proyek;
import model.Tugas;


public class EditTask extends View {
    private String idProyek;
    private static Font poppinsFont;
    int idPengguna = 0;
    private final Proyek proyek;
    private final String idTugas;

    public EditTask(Proyek proyek, String idTugas) {
        this.proyek = proyek; 
        this.idProyek = proyek.getIdProyek();
        this.idTugas = idTugas;
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");

        JFrame frame = new JFrame("Edit Task");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Blue Gradient Panel
        JPanel bluePanel = new GradientPanel(new Color(41, 47, 172), new Color(49, 53, 255));
        bluePanel.setBackground(new Color(41, 47, 172));
        bluePanel.setBounds(0, 0, frame.getWidth(), 50);
        frame.add(bluePanel);

        JLabel titleLabel = new JLabel("Edit Task", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 18));
        bluePanel.add(titleLabel, BorderLayout.CENTER);

        // Judul Tugas Label and TextField
        JLabel judulLabel = new JLabel("Judul Tugas:");
        judulLabel.setBounds(50, 70, 150, 25);
        judulLabel.setFont(poppinsFont);
        frame.add(judulLabel);

        JTextField judulField = new JTextField();
        judulField.setBounds(50, 100, frame.getWidth() - 100, 30);
        judulField.setFont(poppinsFont);
        frame.add(judulField);

        // Deskripsi Tugas Label and TextField
        JLabel descLabel = new JLabel("Deskripsi Tugas:");
        descLabel.setBounds(50, 150, 150, 25);
        descLabel.setFont(poppinsFont);
        frame.add(descLabel);

        JTextField descField = new JTextField();
        descField.setBounds(50, 180, frame.getWidth() - 100, 30);
        descField.setFont(poppinsFont);
        frame.add(descField);

        // Tenggat Waktu Label and Spinner
        JLabel tenggatLabel = new JLabel("Tenggat Waktu:");
        tenggatLabel.setBounds(50, 230, 150, 25);
        tenggatLabel.setFont(poppinsFont);
        frame.add(tenggatLabel);

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setBounds(50, 260, (frame.getWidth() - 160) / 2, 30);
        frame.add(dateSpinner);

        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(frame.getWidth() / 2 + 30, 260, (frame.getWidth() - 160) / 2, 30);
        frame.add(timeSpinner);

        // Prioritas Label and RadioButtons
        JLabel prioritasLabel = new JLabel("Prioritas:");
        prioritasLabel.setBounds(50, 310, 150, 25);
        prioritasLabel.setFont(poppinsFont);
        frame.add(prioritasLabel);

        ButtonGroup priorityGroup = new ButtonGroup();

        JRadioButton tinggiButton = new JRadioButton("Tinggi");
        tinggiButton.setBounds(50, 340, 100, 25);
        tinggiButton.setFont(poppinsFont);
        priorityGroup.add(tinggiButton);
        frame.add(tinggiButton);

        JRadioButton menengahButton = new JRadioButton("Menengah");
        menengahButton.setBounds(150, 340, 100, 25);
        menengahButton.setFont(poppinsFont);
        priorityGroup.add(menengahButton);
        frame.add(menengahButton);

        JRadioButton rendahButton = new JRadioButton("Rendah");
        rendahButton.setBounds(250, 340, 100, 25);
        rendahButton.setFont(poppinsFont);
        priorityGroup.add(rendahButton);
        frame.add(rendahButton);

        // Anggota Tim Label
        JLabel anggotaLabel = new JLabel("Anggota Tim:");
        anggotaLabel.setBounds(50, 390, 150, 25);
        anggotaLabel.setFont(poppinsFont);
        frame.add(anggotaLabel);

        // Scrollable Panel for Team Members
        JPanel anggotaPanel = new JPanel();
        anggotaPanel.setLayout(new BoxLayout(anggotaPanel, BoxLayout.Y_AXIS));
        anggotaPanel.setBackground(Color.WHITE);
        
        List<Anggota> anggotaList = new DatabaseHelper().getAnggotaByProyek(proyek.getIdProyek());
        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (Anggota anggota : anggotaList) {
            JCheckBox checkBox = new JCheckBox(anggota.getUsername());
            checkBox.setActionCommand(String.valueOf(anggota.getIdPengguna()));
            checkBoxes.add(checkBox);
            anggotaPanel.add(checkBox);
        }
        
        Tugas tugas = new DatabaseHelper().getTugasById(idTugas);
        if (tugas != null) {
            judulField.setText(tugas.getNamaTugas());
            descField.setText(tugas.getDeskripsi());
            // Set prioritas berdasarkan nilai tugas
            if ("Tinggi".equals(tugas.getPrioritas())) {
                tinggiButton.setSelected(true);
            } else if ("Menengah".equals(tugas.getPrioritas())) {
                menengahButton.setSelected(true);
            } else {
                rendahButton.setSelected(true);
            }
            // Set tanggal tenggat waktu
            if (tugas.getDeadline() != null) {
                dateSpinner.setValue(tugas.getDeadline());
            }

            // Dapatkan anggota yang sudah terpilih untuk tugas ini
            List<Anggota> tugasAnggota = new DatabaseHelper().getAnggotaByTugas(idTugas);

            // Tandai checkbox yang sesuai
            for (JCheckBox checkBox : checkBoxes) {
                for (Anggota anggota : tugasAnggota) {
                    if (Integer.parseInt(checkBox.getActionCommand()) == anggota.getIdPengguna()) {
                        checkBox.setSelected(true);
                        break;
                    }
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(anggotaPanel);
        scrollPane.setBounds(50, 420, frame.getWidth() - 100, 100);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(scrollPane);

        // Tambah Anggota TextField with Placeholder
        JTextField tambahAnggotaField = new JTextField();
        tambahAnggotaField.setBounds(50, 530, frame.getWidth() - 250, 30);
        tambahAnggotaField.setFont(poppinsFont);
        addPlaceholder(tambahAnggotaField, "Tambah anggota lain...");
        frame.add(tambahAnggotaField);

        // Tambah Button
        JButton tambahButton = new JButton("Tambah");
        tambahButton.setBounds(frame.getWidth() - 190, 530, 100, 30);
        tambahButton.setFont(poppinsFont);
        tambahButton.setBackground(new Color(44, 49, 197));
        tambahButton.setForeground(Color.WHITE);
        frame.add(tambahButton);
        
        tambahButton.addActionListener(e -> {
            String anggotaBaru = tambahAnggotaField.getText().trim();
            if (!anggotaBaru.isEmpty() && !anggotaBaru.equals("Tambah anggota lain...")) {
                addCheckBox(anggotaBaru, anggotaPanel, (ArrayList<JCheckBox>) checkBoxes);
                tambahAnggotaField.setText("");
                anggotaPanel.revalidate();
                anggotaPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Nama anggota tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(frame.getWidth() - 260, 600, 100, 30);
        cancelButton.setFont(poppinsFont);
        cancelButton.setBackground(new Color(44, 49, 197));
        cancelButton.setForeground(Color.WHITE);
        frame.add(cancelButton);
        
        cancelButton.addActionListener(e -> {
            frame.dispose();
            new dashboard(proyek);
        });

        // Save Button
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(frame.getWidth() - 140, 600, 100, 30);
        saveButton.setFont(poppinsFont);
        saveButton.setBackground(new Color(44, 49, 197));
        saveButton.setForeground(Color.WHITE);
        frame.add(saveButton);
        
        saveButton.addActionListener(e -> {
            String namaTugas = judulField.getText();
            String deskripsi = descField.getText();
            Date deadline = (Date) dateSpinner.getValue();
            String prioritas = tinggiButton.isSelected() ? "Tinggi" :
                    menengahButton.isSelected() ? "Menengah" : "Rendah";

            List<Anggota> selectedAnggota = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedAnggota.add(new Anggota(checkBox.getText(), null, null, Integer.parseInt(checkBox.getActionCommand())));
                }
            }

            boolean success = TaskController.updateTask(idTugas, namaTugas, deskripsi, new java.sql.Timestamp(deadline.getTime()), prioritas, selectedAnggota);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Tugas berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new dashboard(proyek);
                
            } else {
                JOptionPane.showMessageDialog(frame, "Gagal memperbarui tugas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add resize listener for responsiveness
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bluePanel.setBounds(0, 0, frame.getWidth(), 50);
                judulField.setBounds(50, 100, frame.getWidth() - 100, 30);
                descField.setBounds(50, 180, frame.getWidth() - 100, 30);
                dateSpinner.setBounds(50, 260, (frame.getWidth() - 160) / 2, 30);
                timeSpinner.setBounds(frame.getWidth() / 2 + 30, 260, (frame.getWidth() - 160) / 2, 30);
                scrollPane.setBounds(50, 420, frame.getWidth() - 100, 100);
                tambahAnggotaField.setBounds(50, 530, frame.getWidth() - 250, 30);
                tambahButton.setBounds(frame.getWidth() - 190, 530, 100, 30);
                cancelButton.setBounds(frame.getWidth() - 260, 600, 100, 30);
                saveButton.setBounds(frame.getWidth() - 140, 600, 100, 30);
                titleLabel.setFont(poppinsFont.deriveFont(18f)); // Fixed font size
            }
        });

        frame.setVisible(true);
    }

    private static void loadFont(String fontPath) {
        try {
            InputStream fontStream = AddProyek.class.getResourceAsStream("/" + fontPath);
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

    private static void addPlaceholder(JTextField textField, String placeholder) {
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
    
    private void addCheckBox(String username, JPanel anggotaPanel, ArrayList<JCheckBox> checkBoxes) {
        JCheckBox checkBox = new JCheckBox(username);
        checkBox.setActionCommand(String.valueOf(idPengguna));
        checkBoxes.add(checkBox);
        anggotaPanel.add(checkBox);
        anggotaPanel.add(Box.createVerticalStrut(5));
    }
}
