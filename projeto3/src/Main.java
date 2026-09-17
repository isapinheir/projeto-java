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

        Projeto p4 = new Projeto(
            4,
            "Portal de Eventos",
            "Portal para eventos",
            "Web",
            "Em desenvolvimento"
        );
        service.adicionar(p4);

        Projeto p5 = new Projeto(
            5,
            "Sistema Financeiro",
            "Sistema para gerenciar finanças",
            "Software",
            "Concluído"
        );
        service.adicionar(p5);

		System.out.println("Total de projetos: " + service.listar().size());
        System.out.println();
        
        System.out.println("LISTA DE PROJETOS");
        for(Projeto projeto : service.listar()){
            projeto.exibirDados();
            System.out.println("--------------");
        }

        System.out.println("BUSCA POR ID");
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
        System.out.println("Total de projetos da categoria: " + service.contarPorCategoria("web"));

        System.out.println("PROJETOS CONCLUÍDOS");
        for(Projeto projeto : service.buscarPorStatus("Concluído")){
            projeto.exibirDados();
        }

        System.out.println("Total de projetos: " + service.listar().size());
        System.out.println("REMOVER PROJETO");
        boolean removido = service.removerPorId(3);
        if(removido){
            System.out.println("Projeto removido com sucesso.");        
        }
        else{
            System.out.println("Projeto não encontrado.");
        }
        System.out.println("Total de projetos: " + service.listar().size());

        boolean cadastrado = service.adicionar(p1);
        if (cadastrado){
            System.out.println("Projeto cadastrado.");
        }
        else{
            System.out.println("Já existe um projeto com esse id.");
        }
        
        System.out.println("ALTERAR STATUS DO PROJETO");
        service.alterarStatus(2, "Em desenvolvimento");
        System.out.println(encontrado.getStatus());

        System.out.println("BUSCAR POR NOME");
        System.out.println(service.buscarPorNome("sistema"));      

    }
}
