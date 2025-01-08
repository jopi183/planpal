package view;

import JDBC.DatabaseHelper;
import auth.SessionManager;
import static auth.SessionManager.logout;
import view.AddProyek;
import view.RoundedButton;
import view.SignIn;
import controller.ProyekController;
import controller.ExportProyekTugas;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import model.Notifikasi;
import model.NotifikasiImplementasi;
import model.Pengguna;
import model.Proyek;

public class halamanproyek extends View {
    private static halamanproyek instance;
    private Font poppinsFont;
    private Notifikasi notifikasi; 
    private Pengguna pengguna;   
    private JPanel proyekListPanel;

    public static halamanproyek getInstance() {
        return instance;  
    }

    public halamanproyek() {
        instance = this;
        pengguna = SessionManager.getCurrentUser();
        notifikasi = new NotifikasiImplementasi();

        notifikasi.kirimPesan("Tugas 'Desain UI' telah selesai!", pengguna);
        
        initComponents();
        
    }
    
    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");

        setTitle("PlanPal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 60, 190));
        headerPanel.setPreferredSize(new Dimension(800, 70));

        JLabel titleLabel = new JLabel("PlanPal");
        titleLabel.setForeground(Color.WHITE);
        setFont(titleLabel, poppinsFont.deriveFont(Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        logoutPanel.setOpaque(false);

        try {
            ImageIcon bellIcon = new ImageIcon(getClass().getResource("/images/bell_icon.png"));
            Image scaledImage = bellIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            bellIcon = new ImageIcon(scaledImage);

            JButton bellButton = new JButton(bellIcon);
            bellButton.setPreferredSize(new Dimension(60, 45));
            bellButton.setBackground(Color.WHITE);
            bellButton.setFocusPainted(false); 
            bellButton.setBorderPainted(false); 

            bellButton.addActionListener(e -> {
                String notifikasiPesan = "Anda memiliki notifikasi baru: Ayo kerjakan tugas dan proyek Anda!\nKelompok 1 PBO IF-46-02";
                notifikasi.kirimPesan(notifikasiPesan, pengguna);
                JOptionPane.showMessageDialog(
                    null,
                    notifikasi.getPesan(),
                    "Notifikasi",
                    JOptionPane.INFORMATION_MESSAGE
                );
            });

            logoutPanel.add(bellButton);

        } catch (Exception e) {
            System.err.println("Gagal memuat ikon lonceng: " + e.getMessage());
            e.printStackTrace();
        }

        RoundedButton logoutButton = new RoundedButton("Logout", 10);
        setFont(logoutButton, poppinsFont.deriveFont(Font.BOLD, 17));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(120, 45));
        logoutPanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                    null,
                    "Terima kasih sudah menggunakan PlanPal, semangat selalu\n- Kelompok 1 PBO - IF-46-02",
                    "Logout Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );

                logout();

                java.awt.EventQueue.invokeLater(() -> {
                    javax.swing.JFrame topFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(logoutButton);
                    if (topFrame != null) {
                        topFrame.dispose();
                    }

                    java.awt.EventQueue.invokeLater(() -> new SignIn().setVisible(true));
                });
            }
        });

        headerPanel.add(logoutPanel, BorderLayout.EAST);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        RoundedButton addProyekButton = new RoundedButton("Add Proyek", 10);
        setFont(addProyekButton, poppinsFont.deriveFont(Font.BOLD, 20));
        addProyekButton.setBackground(new Color(45, 60, 190));
        addProyekButton.setForeground(Color.WHITE);
        addProyekButton.setFocusPainted(false);
        addProyekButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        addProyekButton.setPreferredSize(new Dimension(160, 60));
        addProyekButton.addActionListener(e -> openAddProyekForm());

        RoundedButton exportButton = new RoundedButton("Download Rekap", 10);
        setFont(exportButton, poppinsFont.deriveFont(Font.BOLD, 20));
        exportButton.setBackground(new Color(34, 139, 34));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exportButton.setPreferredSize(new Dimension(160, 60));
        exportButton.addActionListener(e -> {
            String userId = String.valueOf(pengguna.getIdPengguna());
            ExportProyekTugas.exportDataToExcel(userId);
            JOptionPane.showMessageDialog(this, "Rekap berhasil diunduh!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });

        proyekListPanel = new JPanel();
        proyekListPanel.setLayout(new BoxLayout(proyekListPanel, BoxLayout.Y_AXIS));
        proyekListPanel.setBackground(Color.WHITE);

        loadProyekList(proyekListPanel);

        proyekListPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(proyekListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(addProyekButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(exportButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(scrollPane);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
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
            poppinsFont = new Font("Arial", Font.PLAIN, 16); // Fallback font
        }
    }

    private void openAddProyekForm() {
        this.setVisible(false);
        new AddProyek();
    }

    private void logout() {
        SessionManager.logout();
        new SignIn(); 
        this.dispose();
    }

    public void loadProyekList(JPanel proyekListPanel) {
        proyekListPanel.removeAll(); 

        int currentUserID = pengguna.getIdPengguna();

        try {
            DatabaseHelper DBHelper = new DatabaseHelper();
            List<Proyek> proyekList = DBHelper.getProyekByPengguna(currentUserID);

            if (proyekList.isEmpty()) {
                JLabel noProyekLabel = new JLabel("Anda belum memiliki proyek. Tambah proyek baru!");
                setFont(noProyekLabel, poppinsFont.deriveFont(Font.PLAIN, 16));
                proyekListPanel.add(noProyekLabel); // Tambahkan pesan jika tidak ada proyek
            } else {
                for (Proyek proyek : proyekList) {
                    JPanel proyekPanel = createProyekPanel(proyek);
                    proyekListPanel.add(proyekPanel);
                    proyekListPanel.add(Box.createVerticalStrut(10)); // Tambahkan jarak antar proyek
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat proyek!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        proyekListPanel.revalidate(); 
        proyekListPanel.repaint();    
    }

    private JPanel createProyekPanel(Proyek proyek) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(237, 237, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setMaximumSize(new Dimension(1000, 80));

        JLabel label = new JLabel(proyek.getNamaProyek());
        setFont(label, poppinsFont.deriveFont(Font.PLAIN, 16));
        panel.add(label, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    //    buttonPanel.setBackground(new Color(237, 237, 250));

        // Membaca gambar dan mengubah ukurannya agar sesuai dengan tombol
        ImageIcon viewIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/view.png")).getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
        ImageIcon editIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/edit.png")).getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));
        ImageIcon deleteIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/del.png")).getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH));

        // Membuat tombol dengan gambar yang sudah disesuaikan ukurannya
        JButton viewButton = new JButton(viewIcon);
        JButton editButton = new JButton(editIcon);
        JButton deleteButton = new JButton(deleteIcon);

        // Menambahkan ukuran tombol untuk memastikan gambar muat dengan baik
        viewButton.setPreferredSize(new Dimension(40, 25));
        editButton.setPreferredSize(new Dimension(40, 25));
        deleteButton.setPreferredSize(new Dimension(40, 25));

        setFont(viewButton, poppinsFont.deriveFont(Font.PLAIN, 14));
        setFont(editButton, poppinsFont.deriveFont(Font.PLAIN, 14));
        setFont(deleteButton, poppinsFont.deriveFont(Font.PLAIN, 14));

        viewButton.addActionListener(e -> viewProyek(proyek));
        editButton.addActionListener(e -> editProyek(proyek));
        deleteButton.addActionListener(e -> deleteProyek(proyek));

        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        panel.setTransferHandler(new TransferHandler("text"));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TransferHandler handler = panel.getTransferHandler();
                handler.exportAsDrag(panel, e, TransferHandler.MOVE);
            }
        });

        return panel;
    }


    private void viewProyek(Proyek proyek) {
        System.out.println("Viewing project: " + proyek.getNamaProyek());
        new dashboard(proyek).setVisible(true);
        dispose();
    }

    private void deleteProyek(Proyek proyek) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus proyek ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean isDeleted = ProyekController.deleteProyekById(proyek.getIdProyek());
            if (isDeleted) {
                JOptionPane.showMessageDialog(this, "Proyek berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                proyekListPanel.removeAll();
                loadProyekList(proyekListPanel);

                proyekListPanel.revalidate();
                proyekListPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus proyek!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProyek(Proyek proyek) {
        System.out.println("Editing project: " + proyek.getNamaProyek() + " (ID: " + proyek.getIdProyek() + ")");
        new EditProyek(proyek.getIdProyek());
        this.dispose(); 
    }

    private void setFont(JComponent component, Font font) {
        component.setFont(font);
    }

    public static void main(String[] args) {
        new halamanproyek();
    }

    public JPanel getProyekListPanel() {
        return proyekListPanel; // Pastikan proyekListPanel dideklarasikan di kelas
    }
}
