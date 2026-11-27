package barbosa.isabeli.utils;

import java.text.Normalizer;

public final class Textos {

    private Textos() {
    }

    /** Remove acentos, espaços nas pontas e deixa em minúsculas ("Concluído " -> "concluido"). */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.trim().toLowerCase();
    }
}