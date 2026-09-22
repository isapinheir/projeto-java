import model.Projeto;
import dao.ProjetoCSV;
import service.ProjetoService;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws Exception{

        ProjetoService service = new ProjetoService();
        ProjetoCSV dao = new ProjetoCSV();

        service.carregar();

        System.out.println("Projetos Carregados: "+ service.listar().size());
        System.out.println("Os projetos:");
        for(Projeto projeto : service.listar()){
            projeto.exibirDados();
        }

        Projeto novoProjeto = new Projeto(
            6,
            "Novo Projeto",
            "Nova descrição",
            "Software",
            "Em desenvolvimento"
        );

        boolean cadastrado = service.adicionar(novoProjeto);

        if(cadastrado){
            service.salvar();
            System.out.println("Projeto salvo com sucesso.");

        }
        else{
            System.out.println("Não foi possível salvar o projeto.");
        }

        boolean removido = service.removerPorId(1);
        if(removido){
            service.salvar();
        }


        /*Projeto p2 = new Projeto(
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
            "Sistema de gerenciamento para eventos",
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
        System.out.println(service.buscarPorNome("sistema"));*/    

    }
}