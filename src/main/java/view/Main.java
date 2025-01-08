package view;

import java.awt.*;


import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignUp signUpFrame = new SignUp();
            signUpFrame.setVisible(true);
        });
    }
}
