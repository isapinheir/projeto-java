package barbosa.isabeli.utils;

import java.net.URL;

public class Icones {
    public static URL get(String caminho) {
        String caminhoCompleto = "/barbosa/isabeli/assets/img/" + caminho;
        URL url = Icones.class.getResource(caminhoCompleto);
        if (url == null) {
            System.err.println("Ícone não encontrado: " + caminhoCompleto);
        }
        return url;
    }
}