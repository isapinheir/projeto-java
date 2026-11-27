package barbosa.isabeli.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Cartão central (bloco bege com sombra) das telas de login e cadastro.
 * Guarda a escala e desenha o bloco; as subclasses só posicionam os componentes.
 */
public abstract class CartaoAutenticacao extends JPanel {

    public static final int LARGURA = 606;
    public static final int ALTURA = 494;

    protected double escala = 1.0;

    protected CartaoAutenticacao() {
        setLayout(null);
        setOpaque(false);
    }

    public void setEscala(double escala) {
        this.escala = escala;
        repaint();
    }

    /** Converte uma medida do desenho original para a escala atual. */
    protected int px(double valor) {
        return (int) (valor * escala);
    }

    protected Font fonte(int tamanho) {
        return new Font("Arial", Font.BOLD, px(tamanho));
    }

    protected void posicionar(Component c, int x, int y, int largura, int altura) {
        c.setBounds(px(x), px(y), px(largura), px(altura));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        int largura = px(LARGURA);
        int altura = px(ALTURA);

        // Sombra
        g2.setColor(Color.BLACK);
        g2.fillRect(px(20), px(22), largura, altura);

        // Bloco principal
        g2.setColor(new Color(255, 249, 239));
        g2.fillRect(0, 0, largura, altura);

        // Borda
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke((float) escala));
        g2.drawRect(0, 0, largura - 1, altura - 1);

        g2.dispose();
    }
}