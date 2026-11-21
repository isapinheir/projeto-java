package barbosa.isabeli.utils;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.animation.AnimationElement;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;

public class Icones {

    public static ImageIcon get(String nomeArquivo, int largura, int altura, Color novaCor) {
        try {
            URL url = Icones.class.getResource("/barbosa/isabeli/assets/img/" + nomeArquivo);
            if (url == null) return null;

            SVGUniverse universe = new SVGUniverse();
            URI uri = universe.loadSVG(url);
            SVGDiagram diagram = universe.getDiagram(uri);

            String hexColor = String.format("#%02x%02x%02x", 
                novaCor.getRed(), novaCor.getGreen(), novaCor.getBlue());
            
            if(diagram.getRoot() == null) {
            	diagram.getRoot().addAttribute("fill", AnimationElement.AT_XML, hexColor);
            }

            colorir(diagram.getRoot(), hexColor);
            diagram.updateTime(0.0);
            
            BufferedImage image = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            g2d.scale((double) largura / diagram.getWidth(), (double) altura / diagram.getHeight());
            
            diagram.render(g2d);
            g2d.dispose();

            return new ImageIcon(image);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void colorir(SVGElement elemento, String hexColor) throws Exception {
        if (elemento.hasAttribute("fill", AnimationElement.AT_XML)) {
            String fillAtual = elemento.getPresAbsolute("fill").getStringValue();
            if (!"none".equalsIgnoreCase(fillAtual)) {
                elemento.setAttribute("fill", AnimationElement.AT_XML, hexColor);
            }
        }
        if (elemento.hasAttribute("stroke", AnimationElement.AT_XML)) {
            String strokeAtual = elemento.getPresAbsolute("stroke").getStringValue();
            if (!"none".equalsIgnoreCase(strokeAtual)) {
                elemento.setAttribute("stroke", AnimationElement.AT_XML, hexColor);
            }
        }

        for (int i = 0; i < elemento.getNumChildren(); i++) {
            colorir(elemento.getChild(i), hexColor);
        }
    }
    
    public static Icon recolorir(Icon iconeOriginal, Color novaCor) {
        if (!(iconeOriginal instanceof ImageIcon) || novaCor == null) {
            return iconeOriginal;
        }

        int w = iconeOriginal.getIconWidth();
        int h = iconeOriginal.getIconHeight();
        Image img = ((ImageIcon) iconeOriginal).getImage();

        if (w <= 0 || h <= 0 || img == null) {
            return iconeOriginal;
        }

        BufferedImage base = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gBase = base.createGraphics();
        gBase.drawImage(img, 0, 0, w, h, null);
        gBase.dispose();

        BufferedImage tingida = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gTint = tingida.createGraphics();
        gTint.drawImage(base, 0, 0, null);
        gTint.setComposite(AlphaComposite.SrcIn);
        gTint.setColor(novaCor);
        gTint.fillRect(0, 0, w, h);
        gTint.dispose();

        return new ImageIcon(tingida);
    }
}