package barbosa.isabeli.client;

import barbosa.isabeli.model.Projeto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiClient {
	private final HttpClient client;
	private final ObjectMapper mapper;
	private final String baseUrl = "http://localhost:7000";		
	
	public ApiClient(){
		client = HttpClient.newHttpClient();	
		mapper = new ObjectMapper();
	}
	public List<Projeto> listarProjetos() throws Exception{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/projetos")).GET().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return mapper.readValue(response.body(), new TypeReference<List<Projeto>>() {});
	}
	public Projeto cadastrarProjeto(Projeto projeto) throws Exception{
		String json = mapper.writeValueAsString(projeto);
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(baseUrl + "/projetos"))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(json)).build();
		
		HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		return mapper.readValue(response.body(),Projeto.class);
	}
	public void excluirProjeto(int id) throws Exception{
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(baseUrl + "/projetos" + id)).DELETE().build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		if(response.statusCode() != 204) {
			throw new RuntimeException("Erro ao excluir projeto: " + response.statusCode());
		}
	}
}
