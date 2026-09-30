package barbosa.isabeli.api;

import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.service.ProjetoService;

import io.javalin.Javalin;
import java.util.List;

public class Api {

    static ProjetoService service = new ProjetoService();

    public static void raiz(Javalin app) {
        app.get("/", ctx -> {
            ctx.result("API Sistema de Projetos");
        });
    }
    
    public static void listar(Javalin app){
        app.get("/projetos", ctx -> {
        	List<Projeto> projetos = service.listar();
        	ctx.json(projetos);
        });        	
    }
    
    public static void buscarPorId(Javalin app) {
        app.get("/projetos{id}", ctx -> {
        	int id = Integer.parseInt(ctx.pathParam("id"));
        	Projeto projeto = service.buscarPorId(id);
        	
        	if(projeto == null) {
        		ctx.status(404); // a api só devolve o que foi pedido, a mensagem mais amigável deve ser dada por quem chamou a api.
        		return;
        	}
        	ctx.json(projeto);
        });	
    }
}
