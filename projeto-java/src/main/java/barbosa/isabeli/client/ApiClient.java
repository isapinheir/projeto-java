package barbosa.isabeli.client;

import barbosa.isabeli.model.Projeto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl = "http://localhost:7000";

    public ApiClient() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        mapper = new ObjectMapper();
    }

    public List<Projeto> listarProjetos() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/projetos"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        validarStatus(response, "listar projetos", 200);

        return mapper.readValue(response.body(), new TypeReference<List<Projeto>>() {});
    }

    public Projeto cadastrarProjeto(Projeto projeto) throws Exception {
        String json = mapper.writeValueAsString(projeto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/projetos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        validarStatus(response, "cadastrar projeto", 200, 201);

        return mapper.readValue(response.body(), Projeto.class);
    }

    public void excluirProjeto(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/projetos/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204 && response.statusCode() != 200) {
            throw new RuntimeException("Erro ao excluir projeto: " + response.statusCode());
        }
    }

    public Projeto alterarProjeto(int id, Projeto projeto) throws Exception {
        String json = mapper.writeValueAsString(projeto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/projetos/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        validarStatus(response, "alterar projeto", 200);

        return mapper.readValue(response.body(), Projeto.class);
    }

    // ========= LOGS UNIFICADOS (SWING -> JAVALIN) =========

    public void registrarLogSessao(String acao, String detalhes) {
        enviarLogAsync("/api/logs/sessao", acao, detalhes, null);
    }

    public void registrarLogProjeto(String acao, String detalhes) {
        enviarLogAsync("/api/logs/projeto", acao, detalhes, null);
    }

    public void registrarLogErro(String detalhes, Throwable erro) {
        String msgErro = erro != null
                ? (erro.getMessage() != null && !erro.getMessage().isBlank() ? erro.toString() + " - " + erro.getMessage() : erro.toString())
                : null;
        enviarLogAsync("/api/logs/erro", "ERRO", detalhes, msgErro);
    }

    private void enviarLogAsync(String rota, String acao, String detalhes, String erro) {
        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("origem", "SWING");
            payload.put("acao", acao);
            payload.put("detalhes", detalhes);
            payload.put("erro", erro);

            String json = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + rota))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                    .exceptionally(ex -> {
                        System.err.println("Falha ao enviar log para " + rota + ": " + ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            System.err.println("Falha ao serializar log para " + rota + ": " + e.getMessage());
        }
    }

    private void validarStatus(HttpResponse<String> response, String acao, int... codigosEsperados) {
        int status = response.statusCode();
        for (int esperado : codigosEsperados) {
            if (status == esperado) {
                return;
            }
        }
        throw new RuntimeException("Erro ao " + acao + " (HTTP " + status + "): " + response.body());
    }
}