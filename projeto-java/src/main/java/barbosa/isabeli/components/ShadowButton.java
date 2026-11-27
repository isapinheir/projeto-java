package barbosa.isabeli.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

/**
 * Botão com sombra preta e borda, usado nas telas de login e cadastro.
 * Substitui os botões pintados à mão que estavam duplicados em JLogin e JSign.
 */
public class ShadowButton extends JButton {

    private final Color cor;
    private double escala = 1.0;

    public ShadowButton(String texto, Color cor) {
        super(texto);
        this.cor = cor;

        setForeground(new Color(80, 80, 80));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setEscala(double escala) {
        this.escala = escala;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int deslocamento = (int) (6 * escala);
        int largura = getWidth() - deslocamento;
        int altura = getHeight() - deslocamento;

        // Sombra
        g2.setColor(Color.BLACK);
        g2.fillRect(deslocamento, deslocamento, largura, altura);

        // Fundo
        g2.setColor(cor);
        g2.fillRect(0, 0, largura, altura);

        // Borda
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke((float) (2 * escala)));
        g2.drawRect(0, 0, largura - 1, altura - 1);

        g2.dispose();

        super.paintComponent(g);
    }
}