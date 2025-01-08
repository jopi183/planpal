package view;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.InputStream;
import java.util.List;
import model.Anggota;
import model.Proyek;
import model.Tugas;
import JDBC.DatabaseHelper;
import auth.SessionManager;
import static auth.SessionManager.logout;
import controller.TaskController;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Notifikasi;
import model.NotifikasiImplementasi;
import model.Pengguna;

public class dashboard extends View {
    private Font poppinsFont;
    private JPanel taskBoardPanel;
    private Proyek proyek;
    private Notifikasi notifikasi; 
    private Pengguna pengguna;

    public dashboard(Proyek proyek) {
        this.proyek = proyek;
        pengguna = SessionManager.getCurrentUser();
        notifikasi = new NotifikasiImplementasi();
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");
        setTitle("PlanPal - " + proyek.getNamaProyek());
        setSize(1000, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(createHeaderPanel(proyek), BorderLayout.NORTH);
        add(createContentPanel(proyek), BorderLayout.CENTER);

        setVisible(true);
    }
    

    private JPanel createHeaderPanel(Proyek proyek) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 60, 190));
        headerPanel.setPreferredSize(new Dimension(1000, 70));

        JLabel titleLabel = new JLabel(proyek.getNamaProyek());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(poppinsFont.deriveFont(Font.BOLD, 24));
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
        logoutButton.setFont(poppinsFont.deriveFont(Font.BOLD, 17));
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
                    "Terima kasih sudah menggunakan PlanPal, semangat selalu\n Kelompok 1 PBO : IF-46-02 <3",
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
        return headerPanel;
    }

private JPanel createContentPanel(Proyek proyek) {
    DatabaseHelper dbHelper = new DatabaseHelper();
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout());
    contentPanel.setBackground(new Color(240, 240, 240));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 
    topPanel.setBackground(new Color(240, 240, 240));

    JPanel descriptionPanel = createDescriptionPanel(proyek); 
    JPanel membersPanel = createMembersPanel(proyek); 

    topPanel.add(descriptionPanel); 
    topPanel.add(membersPanel);     

    contentPanel.add(topPanel, BorderLayout.NORTH); 

    taskBoardPanel = createTaskBoardPanel(proyek);
    contentPanel.add(taskBoardPanel, BorderLayout.CENTER);

    JPanel addTaskPanel = createAddTaskPanel(proyek);
    contentPanel.add(addTaskPanel, BorderLayout.SOUTH);

    return contentPanel;
}
    private JPanel createMembersPanel(Proyek proyek) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        JPanel membersPanel = new JPanel(new BorderLayout());
        membersPanel.setBackground(Color.WHITE);
        membersPanel.setBorder(BorderFactory.createTitledBorder("Anggota Proyek"));

        DefaultListModel<String> memberListModel = new DefaultListModel<>();
        List<Anggota> anggotaList = dbHelper.getAnggotaByProyek(proyek.getIdProyek());
        for (Anggota anggota : anggotaList) {
            memberListModel.addElement(anggota.getUsername());
        }

        JList<String> memberList = new JList<>(memberListModel);
        memberList.setFont(new Font("Arial", Font.PLAIN, 14));
        memberList.setVisibleRowCount(3); 

        JScrollPane scrollPane = new JScrollPane(memberList);
        scrollPane.setPreferredSize(new Dimension(200, 60)); 
        membersPanel.add(scrollPane, BorderLayout.CENTER);

        return membersPanel;
    }

    private JPanel createTaskBoardPanel(Proyek proyek) {
        JPanel boardPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        boardPanel.setBackground(new Color(240, 240, 240));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boardPanel.add(createTaskColumn("To-Do", proyek, Tugas.Status.TODO));
        boardPanel.add(createTaskColumn("In Progress", proyek, Tugas.Status.IN_PROGRESS));
        boardPanel.add(createTaskColumn("Done", proyek, Tugas.Status.DONE));

        return boardPanel;
    }
    private JPanel createDescriptionPanel(Proyek proyek) {
            JPanel descriptionPanel = new JPanel(new BorderLayout());
            descriptionPanel.setBackground(Color.WHITE);
            descriptionPanel.setBorder(BorderFactory.createTitledBorder("Deskripsi Proyek"));

            JTextArea descriptionTextArea = new JTextArea(proyek.getDeskripsi());
            descriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setWrapStyleWord(true);
            descriptionTextArea.setEditable(false);
            descriptionPanel.add(new JScrollPane(descriptionTextArea), BorderLayout.CENTER);

        return descriptionPanel;
    }


    private JPanel createTaskColumn(String title, Proyek proyek, Tugas.Status status) {
        JPanel columnPanel = new JPanel(new BorderLayout());
        columnPanel.setBackground(Color.WHITE);
        columnPanel.setBorder(BorderFactory.createLineBorder(new Color(41, 47, 172), 2));

        JLabel columnTitle = new JLabel(title, JLabel.CENTER);
        columnTitle.setOpaque(true);
        columnTitle.setBackground(new Color(41, 47, 172));
        columnTitle.setForeground(Color.WHITE);
        columnTitle.setFont(poppinsFont.deriveFont(Font.BOLD, 14));
        columnPanel.add(columnTitle, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(230, 230, 250));

        List<Tugas> tugasList = DatabaseHelper.getTugasByStatus(proyek.getIdProyek(), status.name());
        if (tugasList != null) {
            for (Tugas tugas : tugasList) {
                System.out.println("Tugas" + tugas.getNamaTugas() + " ID : " + tugas.getIdTugas());
                JPanel taskPanel = createTaskPanel(tugas, status, proyek);
                cardPanel.add(taskPanel);
                cardPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        columnPanel.add(scrollPane, BorderLayout.CENTER);

        columnPanel.setTransferHandler(new TaskTransferHandler(status, proyek));
        columnPanel.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent e) {
                try {
                    e.acceptDrop(DnDConstants.ACTION_MOVE);
                    String taskId = (String) e.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    TaskController.updateStatusTask(taskId, status);
                    refreshTaskBoard(proyek); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return columnPanel;
    }

    private JPanel createTaskPanel(Tugas tugas, Tugas.Status status, Proyek proyek) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBackground(Color.WHITE);
        taskPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Ensure it spans the width of the column
        taskPanel.putClientProperty("idTugas", tugas.getIdTugas());

        JLabel taskLabel = new JLabel(tugas.getNamaTugas());
        taskLabel.setFont(poppinsFont.deriveFont(Font.PLAIN, 14));
        taskLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        taskPanel.add(taskLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);

        JButton viewButton = new JButton("View");
        viewButton.setFont(poppinsFont.deriveFont(Font.PLAIN, 12));
        viewButton.setFocusPainted(false);
        viewButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        viewButton.setBackground(new Color(230, 230, 250));

        viewButton.addActionListener(e -> {
            new ViewTask(tugas);
        });

        JButton editButton = new JButton("Edit");
        editButton.setFont(poppinsFont.deriveFont(Font.PLAIN, 12));
        editButton.setFocusPainted(false);
        editButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        editButton.setBackground(new Color(230, 230, 250));
        editButton.addActionListener(e -> {
            this.setVisible(false);
            new EditTask(proyek, tugas.getIdTugas());
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(poppinsFont.deriveFont(Font.PLAIN, 12));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        deleteButton.setBackground(new Color(230, 230, 250));
        deleteButton.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah Anda yakin ingin menghapus task ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmed == JOptionPane.YES_OPTION) {
                boolean success = TaskController.deleteTaskById(tugas.getIdTugas());
                if (success) {
                    refreshTaskBoard(proyek); 
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Penghapusan tugas dibatalkan.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        taskPanel.add(buttonPanel, BorderLayout.EAST);

        taskPanel.setTransferHandler(new TaskTransferHandler(status, proyek));
        taskPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                TransferHandler handler = taskPanel.getTransferHandler();
                handler.exportAsDrag(taskPanel, e, TransferHandler.MOVE);
            }
        });

        return taskPanel;
    }
    
    private JPanel createAddTaskPanel(Proyek proyek) {
        JPanel addTaskPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addTaskPanel.setOpaque(false);

        RoundedButton closeButton = new RoundedButton("Close", 10);
        closeButton.setFont(poppinsFont.deriveFont(Font.BOLD, 17));
        closeButton.setBackground(new Color(45, 60, 190));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(160, 60));

        closeButton.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirmed == JOptionPane.YES_OPTION) {
                new halamanproyek().setVisible(true);
                dispose();
            }
        });

        addTaskPanel.add(closeButton, 0); 

        RoundedButton addTaskButton = new RoundedButton("Add Task", 10);
        addTaskButton.setFont(poppinsFont.deriveFont(Font.BOLD, 17));
        addTaskButton.setBackground(new Color(45, 60, 190));
        addTaskButton.setForeground(Color.WHITE);
        addTaskButton.setFocusPainted(false);
        addTaskButton.setPreferredSize(new Dimension(160, 60));
        addTaskButton.addActionListener(e -> {
            this.setVisible(false);
            new AddTask(proyek);
        });
                addTaskPanel.add(addTaskButton);

        return addTaskPanel;
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

    private void refreshTaskBoard(Proyek proyek) {
        taskBoardPanel.removeAll();
        taskBoardPanel.add(createTaskColumn("To-Do", proyek, Tugas.Status.TODO));
        taskBoardPanel.add(createTaskColumn("In Progress", proyek, Tugas.Status.IN_PROGRESS));
        taskBoardPanel.add(createTaskColumn("Done", proyek, Tugas.Status.DONE));
        taskBoardPanel.revalidate();
        taskBoardPanel.repaint();
    }

    private class TaskTransferHandler extends TransferHandler {
        private Tugas.Status targetStatus;
        private Proyek proyek;

        public TaskTransferHandler(Tugas.Status targetStatus, Proyek proyek) {
            this.targetStatus = targetStatus;
            this.proyek = proyek;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            JPanel taskPanel = (JPanel) c;
            String idTugas = (String) taskPanel.getClientProperty("idTugas");
            return new StringSelection(idTugas);
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            try {
                String idTugas = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                TaskController.updateStatusTask(idTugas, targetStatus);
                refreshTaskBoard(proyek);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static void main(String[] args) {
        Anggota anggota1 = new Anggota("user1", "user1@email.com", "password1", 1);
        Anggota anggota2 = new Anggota("user2", "user2@email.com", "password2", 2);

        Proyek proyek = new Proyek("1", "Proyek A", "Description of Proyek A", List.of(anggota1, anggota2));

        new dashboard(proyek);
    }
}
