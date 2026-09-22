import model.Projeto;
import dao.ProjetoCSV;
import service.ProjetoService;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception{

        ProjetoService service = new ProjetoService();
        ProjetoCSV dao = new ProjetoCSV();
        Scanner sc = new Scanner(System.in);
        int opt;

        service.carregar();

        System.out.println("Total de projetos: "+ service.listar().size());
        System.out.println();

        do{
            System.out.println("1- Listar");
            System.out.println("2- Buscar");
            System.out.println("3- Cadastrar");
            System.out.println("4- Alterar");
            System.out.println("5- Excluir");
            System.out.println("0- Sair");

            System.out.print("Escolha --> ");
            opt = sc.nextInt();
        }
        while(opt != 0);


       /* 
        System.out.println("=== Projetos ===");
        for(Projeto projeto : service.listar()){
            projeto.exibirDados();
            System.out.println("--------------");
        }
       
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
        else{
            System.out.println("Não foi possível salvar o projeto.");
        }


        Projeto encontrado = service.buscarPorId(2);
        if(encontrado != null){
            System.out.println("Projeto encontrado:");
            encontrado.exibirDados();        
        }
        else{
            System.out.println("Projeto não encontrado.");      
        }

        for(Projeto projeto : service.buscarPorCategoria("Web")){
            projeto.exibirDados();
        }
        System.out.println("Total de projetos da categoria: " + service.contarPorCategoria("web"));

        System.out.println("PROJETOS CONCLUÍDOS");
        for(Projeto projeto : service.buscarPorStatus("Concluído")){
            projeto.exibirDados();
        }


        boolean removido = service.removerPorId(3);
        if(removido){
            System.out.println("Projeto removido com sucesso.");        
        }
        else{
            System.out.println("Projeto não encontrado.");
        }

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