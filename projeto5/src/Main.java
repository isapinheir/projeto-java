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
        System.out.println("============================");
        System.out.println("=== PORTIFÓLIO ACADÊMICO ===");
        System.out.println("============================");
        System.out.println("= Bem-vindo(a) ao sistema! =");
        System.out.println("");
        System.out.println("Total de projetos: "+ service.listar().size());
        System.out.println("");

        do{
            System.out.println("1- Listar");
            System.out.println("2- Buscar");
            System.out.println("3- Cadastrar");
            System.out.println("4- Alterar");
            System.out.println("5- Excluir");
            System.out.println("0- Sair");

            System.out.print("Escolha --> ");
            opt = sc.nextInt();

            switch(opt){
                case 1:
                    System.out.println("=== Projetos ===");
                    for(Projeto projeto : service.listar()){
                        projeto.exibirDados();
                        System.out.println("-------------------------");
                    }
                    break;
                case 2:
                    System.out.println("=== Busca ===");
                    System.out.print("Digite o id do projeto que deseja buscar: ");
                    int busca = sc.nextInt();
                    System.out.println("");

                    Projeto projeto = service.buscarPorId(busca);
                    if(projeto != null){
                        projeto.exibirDados(); 
                        System.out.println("-------------------------");       
                    }
                    else{
                        System.out.println("Não foi possível buscar o projeto.");
                        System.out.println("-------------------------");
                    }
                    break;
                case 3:
                    System.out.println("=== CADASTRO ===");

                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Descrição: ");
                    String desc = sc.nextLine();

                    System.out.print("Categoria: ");
                    String cat = sc.nextLine();

                    System.out.print("Status: ");
                    String stts = sc.nextLine();

                    Projeto novoProjeto = new Projeto(id, nome, desc, cat, stts);

                    boolean cadastrado = service.adicionar(novoProjeto);
                    if(cadastrado){
                        service.salvar();
                        System.out.println("Projeto salvo com sucesso.");
                        System.out.println("-------------------------");
                    }
                    else{
                        System.out.println("Não foi possível salvar o projeto.");
                        System.out.println("-------------------------");
                    }
                    break;
                case 4:
                    System.out.println("=== ALTERAR PROJETO ===");

                    System.out.print("Informe a id do projeto a ser alterado: ");
                    id = sc.nextInt();
                    sc.nextLine();

                    Projeto existente = service.buscarPorId(id);
                    if(existente == null){
                        System.out.println("Não foi possível encontrar o projeto.");
                        System.out.println("-------------------------");
                        break;
                    }
                    System.out.println("Dados Atuais:");
                    existente.exibirDados();
                    System.out.println("-------------------------");

                    System.out.println("Novos Dados:");
                    System.out.print("Nome: ");
                    nome = sc.nextLine();

                    System.out.print("Descrição: ");
                    desc = sc.nextLine();

                    System.out.print("Categoria: ");
                    cat = sc.nextLine();

                    System.out.print("Status: ");
                    stts = sc.nextLine();

                    Projeto alterado = new Projeto(id, nome, desc, cat, stts);
                    boolean alterado = serivce.alterar(atualizado);
                    if(alterado){
                        service.salvar();
                        System.out.println("Projeto alterado.");
                    }
                    else{
                        System.out.println("Não foi possível alterar o projeto.");
                    }

            }
        }
        while(opt != 0);

       /* 
        boolean removido = service.removerPorId(1);
        if(removido){
            service.salvar();
        }
        else{
            System.out.println("Não foi possível salvar o projeto.");
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