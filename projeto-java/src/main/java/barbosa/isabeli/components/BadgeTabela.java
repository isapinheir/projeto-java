package barbosa.isabeli.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.text.Normalizer;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import barbosa.isabeli.utils.Cores;
import barbosa.isabeli.utils.Fontes;

public class BadgeTabela extends DefaultTableCellRenderer {
        private final boolean isStatus;
        private final Font fonteBadge;

        BadgeTabela(boolean isStatus) {
            this.isStatus = isStatus;
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            this.fonteBadge = Fontes.carregarFonte("Poppins-Regular.ttf", 14f);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            String texto = value != null ? value.toString() : "";
            String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase();

            return new JPanel() {
                {
                    setOpaque(false);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    Color fundo;
                    Color borda = new Color(59, 59, 62);

                    if (!isStatus) {
                        fundo = new Color(241, 245, 249);
                    } else if ("concluido".equals(normalizado)) {
                        fundo = Cores.get("verde");
                    } else if ("em desenvolvimento".equals(normalizado)) {
                        fundo = Cores.get("amarelo");
                    } else {
                        fundo = Cores.get("roxo");
                    }

                    g2.setFont(fonteBadge);
                    FontMetrics fm = g2.getFontMetrics();

                    int paddingX = 14; 
                    int paddingY = 8; 
                    int margemLateral = 6;

                    int maxLarguraDisponivel = Math.max(20, getWidth() - (margemLateral * 2));
                    int pillW = Math.min(fm.stringWidth(texto) + (paddingX * 2), maxLarguraDisponivel);
                    int pillH = Math.min(fm.getHeight() + paddingY, Math.max(20, getHeight() - 8));

                    int px = (getWidth() - pillW) / 2;
                    int py = (getHeight() - pillH) / 2;

                    Shape pill = new RoundRectangle2D.Double(px, py, pillW, pillH, pillH, pillH);
                    g2.setColor(fundo);
                    g2.fill(pill);

                    if (isStatus) {
                        g2.setColor(borda);
                        g2.draw(pill);
                    }

                    g2.setColor(Cores.get("textoSecundario"));
                    int textX = px + ((pillW - fm.stringWidth(texto)) / 2);
                    int textY = py + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(texto, textX, textY);

                    g2.dispose();
                }
            };
        }
    }