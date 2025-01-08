package view;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private final int radius; // Besarnya lengkungan

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setFocusPainted(false); 
        setContentAreaFilled(false); 
        setBorderPainted(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        if (getModel().isPressed()) {
            g2.setColor(new Color(0, 0, 0, 50)); 
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }

        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
