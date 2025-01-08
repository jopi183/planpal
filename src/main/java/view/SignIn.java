package view;

import JDBC.sqlconnection;
import auth.LoginService;
import auth.SessionManager;
import controller.LoginController;
import model.Pengguna;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.Connection;


public class SignIn extends View {

    private Font poppinsFont; // Font Poppins

    public SignIn() {
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        // Memuat font Poppins
        loadFont("fonts/Poppins-Regular.ttf");

        setTitle("Sign In");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/signintext.png"));
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 60, Image.SCALE_SMOOTH); // Sesuaikan ukuran (200x60 contoh)
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel signInLabel = new JLabel(resizedIcon);
        signInLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel instructions = new JLabel("Masukkan data Anda untuk masuk ke dalam aplikasi PlanPal");
        setFont(instructions, poppinsFont.deriveFont(17f));

        JLabel usernameLabel = new JLabel("Username");
        setFont(usernameLabel, poppinsFont.deriveFont(Font.BOLD, 18f));

        JTextField usernameField = new JTextField();
        setFont(usernameField, poppinsFont);
        usernameField.setPreferredSize(new Dimension(600, 44));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(new RoundedBorder(15));
        setPadding(usernameField, new Insets(5, 10, 5, 10));

        JLabel passwordLabel = new JLabel("Password");
        setFont(passwordLabel, poppinsFont.deriveFont(Font.BOLD, 18f));

        JPasswordField passwordField = new JPasswordField();
        setFont(passwordField, poppinsFont);
        passwordField.setPreferredSize(new Dimension(600, 44));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(new RoundedBorder(15));
        setPadding(passwordField, new Insets(5, 10, 5, 10));

        JButton signInButton = new RoundedButton("Sign In", 50);
        signInButton.setFont(poppinsFont.deriveFont(Font.BOLD, 16f));
        signInButton.setPreferredSize(new Dimension(150, 40));
        signInButton.setBackground(Color.decode("#292FAC"));
        signInButton.setForeground(Color.WHITE);
        
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                LoginService loginService = new LoginService(sqlconnection.connectdb(), username, password);
                if (loginService.Authenticate()) {
                    int userID = loginService.getUserID(); 
                    SessionManager.setCurrentUserID(userID); 
                    Pengguna pengguna = loginService.getPengguna();  
                    SessionManager.setCurrentUser(pengguna);  
                    new halamanproyek().setVisible(true);
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(SignIn.this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }

                sqlconnection.disconnect();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(signInLabel, gbc);

        gbc.gridy++;
        leftPanel.add(instructions, gbc);

        gbc.gridy++;
        leftPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        leftPanel.add(usernameField, gbc);

        gbc.gridy++;
        leftPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        leftPanel.add(passwordField, gbc);

        gbc.gridy++;
        leftPanel.add(signInButton, gbc);

        // Panel kanan untuk dekorasi
        JPanel rightPanel = new GradientPanel(new Color(41, 47, 172), new Color(49, 53, 255));
        rightPanel.setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("PlanPal");
        setFont(welcomeLabel, poppinsFont.deriveFont(Font.BOLD, 48f));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel welcomeMessage = new JLabel("Halo, selamat datang di aplikasi PlanPal");
        setFont(welcomeMessage, poppinsFont.deriveFont(18f));
        welcomeMessage.setForeground(Color.WHITE);

        JLabel signUpMessage = new JLabel("Daftarkan diri Anda dan mulai gunakan layanan kami segera");
        setFont(signUpMessage, poppinsFont.deriveFont(15f));
        signUpMessage.setForeground(Color.WHITE);

        ImageIcon signUpIcon = new ImageIcon(getClass().getResource("/images/signup.png"));
        JButton signUpButton = new JButton(signUpIcon);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setBorder(BorderFactory.createEmptyBorder());
        signUpButton.setFocusPainted(false);
        signUpButton.setPreferredSize(new Dimension(500, 90));
        
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUp().setVisible(true);
                dispose(); 
            }
        });

        ImageIcon planPalLogo = new ImageIcon(getClass().getResource("/images/planpal.png"));
        Image resizedLogoImage = planPalLogo.getImage().getScaledInstance(350, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);
        JLabel logoLabel = new JLabel(resizedLogoIcon);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.insets = new Insets(10, 10, 10, 10);
        gbcRight.anchor = GridBagConstraints.CENTER;

        rightPanel.add(logoLabel, gbcRight);

        gbcRight.gridy++;
        rightPanel.add(welcomeMessage, gbcRight);

        gbcRight.gridy++;
        rightPanel.add(signUpMessage, gbcRight);

        gbcRight.gridy++;
        gbcRight.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(signUpButton, gbcRight);

        // Tambahkan panel ke frame
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5; // Mengatur berat untuk responsivitas
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(leftPanel, gbc);

        gbc.gridx = 1;
        add(rightPanel, gbc);
    }


    private void loadFont(String fontPath) {
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            System.out.println("Font path: " + getClass().getClassLoader().getResource(fontPath));

            if (fontStream == null) {
                throw new Exception("Font file not found: " + fontPath);
            }

            poppinsFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(poppinsFont);

            System.out.println("Font berhasil dimuat: " + fontPath);
        } catch (Exception e) {
            System.err.println("Gagal memuat font: " + fontPath);
            e.printStackTrace();
            poppinsFont = new Font("Arial", Font.PLAIN, 16); // Fallback jika gagal
        }
    }

    private void setFont(JComponent component, Font font) {
        component.setFont(font);
    }

    private void setPadding(JTextComponent component, Insets insets) {
        component.setMargin(insets);
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignIn().setVisible(true);
        });
    }
}