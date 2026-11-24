package barbosa.isabeli.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Fontes {

    public static Font carregarFonte(String fonte, float size) {
        try {
        	String path = "/barbosa/isabeli/assets/fonts/" + fonte;
            InputStream is = Fontes.class.getResourceAsStream(path);
            
            if (is == null) {
                is = new java.io.FileInputStream(new File(path));
            }

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            return customFont.deriveFont(size);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size);
        }
    }
}
