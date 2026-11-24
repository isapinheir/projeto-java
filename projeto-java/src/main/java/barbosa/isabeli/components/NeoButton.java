package barbosa.isabeli.components;

import barbosa.isabeli.utils.Fontes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class NeoButton extends JButton {
    private boolean isPressed = false;
    private Color corFundo;

    public NeoButton(String text, Color cor) {
        super(text);
        this.corFundo = cor;
        
        setFont(Fontes.carregarFonte("Orbitron-ExtraBold.ttf", 20f));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadowOffset = isPressed ? 2 : 5;
        int drawOffsetX = isPressed ? 3 : 0;
        int drawOffsetY = isPressed ? 3 : 0;

        int width = getWidth() - 6;
        int height = getHeight() - 6;

        // sombra
        g2d.setColor(Color.BLACK);
        g2d.fillRect(shadowOffset + drawOffsetX, shadowOffset + drawOffsetY, width, height);

        // fundo
        g2d.setColor(corFundo);
        g2d.fillRect(drawOffsetX, drawOffsetY, width, height);

        // borda
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(drawOffsetX, drawOffsetY, width, height);

        // texto
        g2d.setColor(getForeground());
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2 + drawOffsetX;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent() + drawOffsetY;
        g2d.drawString(getText(), textX, textY);

        g2d.dispose();
    }
}
