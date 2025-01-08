package view;

import auth.RegisterManager;
import controller.RegisterController;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.swing.border.AbstractBorder;
import javax.swing.text.JTextComponent;

public class SignUp extends View {
    private Font poppinsFont;

    public SignUp() {
        initComponents();
    }
    
    @Override
    protected void initComponents() {
        loadFont("fonts/Poppins-Regular.ttf");

        setTitle("Sign Up");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2)); // Two panels side by side
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel kiri untuk "Sign In"
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/signuptext.png"));
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 60, Image.SCALE_SMOOTH); // Resize image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        
        JLabel signInLabel = new JLabel(resizedIcon);
        signInLabel.setHorizontalAlignment(JLabel.LEFT);

        JLabel instructions = new JLabel("Masukkan data Anda untuk daftar ke dalam aplikasi PlanPal");
        setFont(instructions, poppinsFont.deriveFont(20f));

        JLabel usernameLabel = new JLabel("Username");
        setFont(usernameLabel, poppinsFont.deriveFont(Font.BOLD, 16f));

        JTextField usernameField = new JTextField();
        setFont(usernameField, poppinsFont);
        usernameField.setPreferredSize(new Dimension(600, 45));
        usernameField.setBorder(new RoundedBorder(15));
        setPadding(usernameField, new Insets(5, 10, 5, 10));

        JLabel emailLabel = new JLabel("Email");
        setFont(emailLabel, poppinsFont.deriveFont(Font.BOLD, 16f));

        JTextField emailField = new JTextField();
        setFont(emailField, poppinsFont);
        emailField.setPreferredSize(new Dimension(600, 45));
        emailField.setBorder(new RoundedBorder(15));
        setPadding(emailField, new Insets(5, 10, 5, 10));

        JLabel passwordLabel = new JLabel("Password");
        setFont(passwordLabel, poppinsFont.deriveFont(Font.BOLD, 16f));

        JPasswordField passwordField = new JPasswordField();
        setFont(passwordField, poppinsFont);
        passwordField.setPreferredSize(new Dimension(600, 45));
        passwordField.setBorder(new RoundedBorder(15));
        setPadding(passwordField, new Insets(5, 10, 5, 10));

        JButton signUpButton = new RoundedButton("Sign Up", 50);
        signUpButton.setFont(poppinsFont.deriveFont(Font.BOLD, 16f));
        signUpButton.setPreferredSize(new Dimension(150, 50));
        signUpButton.setBackground(Color.decode("#292FAC"));
        signUpButton.setForeground(Color.WHITE);
        
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua harus harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RegisterManager registerManager = new RegisterManager();
            RegisterController registerController = new RegisterController(registerManager);

            String resultMessage = registerController.registerUser(username, email, password);

            if ("Registration successful.".equalsIgnoreCase(resultMessage)) {
                JOptionPane.showMessageDialog(this, resultMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                SwingUtilities.invokeLater(() -> new SignIn().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, resultMessage, "Error", JOptionPane.ERROR_MESSAGE);
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
        leftPanel.add(emailLabel, gbc);

        gbc.gridy++;
        leftPanel.add(emailField, gbc);

        gbc.gridy++;
        leftPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        leftPanel.add(passwordField, gbc);

        gbc.gridy++;
        leftPanel.add(signUpButton, gbc);

        // Panel kanan untuk dekorasi
        JPanel rightPanel = new GradientPanel(new Color(41, 47, 172), new Color(49, 53, 255));
        rightPanel.setLayout(new GridBagLayout()); // Using GridBagLayout for more control

        JLabel welcomeMessage = new JLabel("Halo, selamat datang di aplikasi PlanPal");
        setFont(welcomeMessage, poppinsFont.deriveFont(20f));
        welcomeMessage.setForeground(Color.WHITE);

        JLabel signUpMessage = new JLabel("Daftarkan diri Anda dan mulai gunakan layanan kami segera");
        setFont(signUpMessage, poppinsFont.deriveFont(15f));
        signUpMessage.setForeground(Color.WHITE);

        ImageIcon signInIcon = new ImageIcon(getClass().getResource("/images/signinbutton.png"));
        JButton signInButton = new JButton(signInIcon);
        signInButton.setContentAreaFilled(false); // Transparent button
        signInButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        signInButton.setFocusPainted(false); // Remove focus effect
        signInButton.setPreferredSize(new Dimension(500, 90));
        
        signInButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new SignIn().setVisible(true));
        });
        
        // Resize the PlanPal logo
        ImageIcon planPalLogo = new ImageIcon(getClass().getResource("/images/planpal.png"));
        Image resizedLogoImage = planPalLogo.getImage().getScaledInstance(350, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);
        JLabel logoLabel = new JLabel(resizedLogoIcon);

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.insets = new Insets(10, 10, 10, 10);
        gbcRight.anchor = GridBagConstraints.CENTER; // Center the content

        rightPanel.add(logoLabel, gbcRight); // Add the logo in the center

        gbcRight.gridy++;
        rightPanel.add(welcomeMessage, gbcRight);

        gbcRight.gridy++;
        rightPanel.add(signUpMessage, gbcRight);

        gbcRight.gridy++;
        gbcRight.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(signInButton, gbcRight);

        // Add panels to the frame
        add(leftPanel);
        add(rightPanel);
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
            poppinsFont = new Font("Arial", Font.PLAIN, 16); // Fallback if font loading fails
        }
    }

    private void setFont(JComponent component, Font font) {
        component.setFont(font);
    }

    private void setPadding(JTextComponent component, Insets insets) {
        component.setMargin(insets);
    }

    // Custom rounded border
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
            new SignUp().setVisible(true);
        });
    }
}