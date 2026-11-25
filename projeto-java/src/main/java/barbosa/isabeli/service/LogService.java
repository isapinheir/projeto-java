package barbosa.isabeli.service;

import barbosa.isabeli.model.LogReq;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {

    private static final Path PASTA_LOGS = Paths.get("logs");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Path ARQUIVO_SESSOES = PASTA_LOGS.resolve("sessoes.log");
    private static final Path ARQUIVO_PROJETOS = PASTA_LOGS.resolve("projetos.log");
    private static final Path ARQUIVO_ERROS = PASTA_LOGS.resolve("erros.log");

    private static final Object LOCK = new Object();

    public LogService() {
        try {
            Files.createDirectories(PASTA_LOGS);
        } catch (IOException e) {
            System.err.println("Não foi possível criar a pasta de logs: " + e.getMessage());
        }
    }
    
    public void registrarSessao(LogReq req) {
        escrever(ARQUIVO_SESSOES, formatar(req));
    }

    public void registrarProjeto(LogReq req) {
        escrever(ARQUIVO_PROJETOS, formatar(req));
    }

    public void registrarErro(LogReq req) {
        escrever(ARQUIVO_ERROS, formatar(req));
    }

    private String formatar(LogReq req) {
        String agora = LocalDateTime.now().format(FORMATO_DATA);
        String origem = normalizarCampo(req.origem(), "DESCONHECIDA").toUpperCase();
        String acao = normalizarCampo(req.acao(), "EVENTO").toUpperCase();
        String detalhes = normalizarCampo(req.detalhes(), "-");

        String base = String.format("%s | ORIGEM=%s | ACAO=%s | DETALHES=%s",
                agora, origem, acao, detalhes);

        if (req.erro() != null && !req.erro().isBlank()) {
            String erroNorm = req.erro().replace("\r\n", " ").replace("\n", " ").trim();
            return base + " | ERRO=" + erroNorm;
        }
        return base;
    }
    
    private String normalizarCampo(String valor, String fallback) {
        if (valor == null || valor.isBlank()) {
            return fallback;
        }
        return valor.replace("\r\n", " ").replace("\n", " ").trim();
    }

    private static void escrever(Path arquivo, String linha) {
        synchronized (LOCK) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    arquivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            )) {
                writer.write(linha);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Falha ao escrever log em " + arquivo + ": " + e.getMessage());
            }
        }
    }
}