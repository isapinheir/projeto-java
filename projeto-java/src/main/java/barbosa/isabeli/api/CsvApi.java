package barbosa.isabeli.api;

import io.javalin.Javalin;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvApi {

    public static void registrarRotas(Javalin app) {
        
        app.get("/ver-csv", ctx -> {
            // O caminho relativo a partir da pasta 'resources'
            String caminhoArquivo = "/projetos.csv"; // Substitua pelo nome exato do seu arquivo
            
            StringBuilder conteudoHtml = new StringBuilder();
            conteudoHtml.append("<h2>Conteúdo do CSV:</h2><br>");

            try (InputStream inputStream = CsvApi.class.getResourceAsStream(caminhoArquivo)) {
                if (inputStream == null) {
                    ctx.status(404).result("Arquivo CSV não encontrado na pasta resources/dados!");
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        // Adiciona cada linha do CSV com uma quebra de linha para o navegador
                        conteudoHtml.append(linha).append("<br>");
                    }
                }

                // Envia o texto completo para o navegador
                ctx.html(conteudoHtml.toString());

            } catch (Exception e) {
                ctx.status(500).result("Erro ao ler o arquivo: " + e.getMessage());
            }
        });
    }
}