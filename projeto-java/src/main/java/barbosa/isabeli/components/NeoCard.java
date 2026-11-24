package barbosa.isabeli.components;

import barbosa.isabeli.utils.Cores;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class NeoCard extends JPanel {
	private Color fundo;
	
    public NeoCard(Color cor) {
        setOpaque(false);
        this.fundo = cor;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        // Ativa antialiasing para as bordas ficarem limpas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth() - 6;
        int height = getHeight() - 6;

        // 1. Desenha a sombra preta deslocada (Hard Shadow)
        g2d.setColor(Color.BLACK);
        g2d.fillRect(12, 12, width, height);

        // 2. Desenha o fundo do cartão (Amarelo claro pastel)
        g2d.setColor(fundo);
        g2d.fillRect(0, 0, width, height);

        // 3. Desenha a borda preta grossa (3 pixels)
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width, height);

        g2d.dispose();
    }
}
