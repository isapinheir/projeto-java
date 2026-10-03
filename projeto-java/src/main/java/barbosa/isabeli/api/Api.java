package barbosa.isabeli.api;

import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.service.ProjetoService;
import io.javalin.Javalin;
import java.util.List;

public class Api {

    static ProjetoService service = new ProjetoService();

    public static void web(Javalin app) {
    	// raiz
        app.get("/", ctx -> {
            ctx.result("API Sistema de Projetos");
        });
        // POST
        app.post("/projetos", ctx -> {
        	Projeto projeto = ctx.bodyAsClass(Projeto.class);
        	
        	if (projeto.getNome() == null || projeto.getNome().isBlank()) {
    		    ctx.status(400);
    		    ctx.json("{\"erro\":\"Nome é obrigatório\"}");
    		    return;
    		}
        	service.adicionar(projeto);
        	service.salvar();
        	ctx.status(201);
        	ctx.json(projeto);
        });
        // GET
        app.get("/projetos", ctx -> {
	        try {
	            List<Projeto> projetos = service.listar();
	            ctx.json(projetos);
	        } 
	        catch (Exception e) {
	            e.printStackTrace();
	            ctx.status(500).result("Erro ao listar projetos.");
	        }
	    });
        // GET por id
        app.get("/projetos/{id}", ctx -> {
        	int id = Integer.parseInt(ctx.pathParam("id"));
        	Projeto projeto = service.buscarPorId(id);
        	
        	if(projeto == null) {
        		ctx.status(404); // a api só devolve o que foi pedido, a mensagem mais amigável deve ser dada por quem chamou a api.
        		return;
        	}
        	ctx.json(projeto);
        });
        // PUT
        app.put("/projetos/{id}", ctx -> {
        	int id = Integer.parseInt(ctx.pathParam("id"));
        	Projeto projeto = ctx.bodyAsClass(Projeto.class);
        	projeto.setId(id);
        	
        	boolean alterou = service.alterarProjeto(projeto);
        	if(!alterou) {
        		ctx.status(404);
        		return;
        	}
        	service.salvar();
        	ctx.json(projeto);
        });
        // DELETE
        app.delete("/projetos/{id}", ctx ->{
        	int id = Integer.parseInt(ctx.pathParam("id"));
        	Projeto projeto = service.buscarPorId(id);
        	if(projeto == null) {
        		ctx.status(404);
        		return;
        	}
        	service.removerPorId(id);
        	service.salvar();
        	ctx.status(204);
        });
        
    }
}