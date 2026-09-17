import model.Projeto;
import service.ProjetoService;

public class Main {

    public static void main(String[] args) {

        ProjetoService service = new ProjetoService();

        Projeto p1 = new Projeto(
            1,
            "Sistema Acadêmico",
            "Software para gerenciamento acadêmico",
            "Software",
            "Em desenvolvimento"
        );
        service.adicionar(p1);

        Projeto p2 = new Projeto(
            2,
            "Site Institucional",
            "Site de uma instituição",
            "Web",
            "Concluído"
        );
        service.adicionar(p2);

        Projeto p3 = new Projeto(
            3,
            "Aplicativo Mobile",
            "Aplicativo de serviços",
            "Mobile",
            "Planejado"
        );
        service.adicionar(p3);

        for(Projeto projeto : service.listar()){
			projeto.exibirDados();
		}
		System.out.println("Total de projetos: " + service.listar().size());

        Projeto encontrado = service.buscarPorId(2);
        if(encontrado != null){
            System.out.println("Projeto encontrado:");
            encontrado.exibirDados();        
        }
        else{
            System.out.println("Projeto não encontrado.");      
        }

        System.out.println("PROJETOS WEB");
        for(Projeto projeto : service.buscarPorCategoria("Web")){
            projeto.exibirDados();
        }

        System.out.println("PROJETOS CONCLUÍDOS");
        for(Projeto projeto : service.buscarPorStatus("Concluído")){
            projeto.exibirDados();
        }

        System.out.println("Total de projetos: " + service.listar().size());
        boolean removido = service.removerPorId(2);
        if(removido){
            System.out.println("Projeto removido com sucesso.");        
        }
        else{
            System.out.println("Projeto não encontrado.");
        }
        System.out.println("Total de projetos: " + service.listar().size());
    }
}


/*
cadastro de projetos
listar todos
exibir quantidade - total
buscar por id
listar por categoria
remover por id
lidar com cadastro de ids iguais

exibir quantidade - categoria
alterar status
exibir por nome
*/