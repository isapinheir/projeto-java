import model.Projeto;
import dao.ProjetoCSV;
import service.ProjetoService;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    static ProjetoService service = new ProjetoService();
    static ProjetoCSV dao = new ProjetoCSV();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        int opt, opt2;

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
                    opcaoListar();
                    break;
                case 2:
                    System.out.println("=== Busca ===");
                    System.out.println("Como deseja realizar sua busca?");
                    System.out.println("1- Por id");
                    System.out.println("2- Por categoria");

                    System.out.print("Escolha --> ");
                    opt2 = sc.nextInt();

                    switch(opt2){
                        case 1:
                            opcaoBuscaId();
                            break;
                        case 2:
                            opcaoBuscaCategoria();
                            break;
                        default:                       
                            System.out.println("Opção inválida.");
                            break;
                    }
                    break;
                case 3:
                    opcaoCadastro();
                    break;
                case 4:
                    opcaoAlterar();
                    break;
                case 5:
                    opcaoDeletar();
                    break;
                case 0:
                    System.out.println("-------------------------");
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
        while(opt != 0);

       /* 
        System.out.println("PROJETOS CONCLUÍDOS");
        for(Projeto projeto : service.buscarPorStatus("Concluído")){
            projeto.exibirDados();
        }

        System.out.println("BUSCAR POR NOME");
        System.out.println(service.buscarPorNome("sistema"));
                            System.out.println("3- Por descrição");
                    System.out.println("4- Por categoria");
                    System.out.println("5- Por status");
        */    
    }
    public static void opcaoListar(){
        System.out.println("=== Projetos ===");
        for(Projeto projeto : service.listar()){
            projeto.exibirDados();
            System.out.println("-------------------------");
        }
    }
    public static void opcaoBuscaId(){
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
    }
    public static void opcaoBuscaCategoria(){
        System.out.print("Digite o nome do projeto que deseja buscar: ");
        sc.nextLine();
        String cat = sc.nextLine();

        for(Projeto projeto : service.buscarPorCategoria(cat)){
            projeto.exibirDados();
            System.out.println("-------------------------");    
        }

        System.out.println("Total de projetos da categoria: " + service.contarPorCategoria(cat));
        System.out.println("-------------------------");
    }
    public static void opcaoCadastro() throws Exception{
        System.out.println("=== Cadastro ===");

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

        Projeto projeto = new Projeto(id, nome, desc, cat, stts);

        boolean cadastrado = service.adicionar(projeto);
        if(cadastrado){
            service.salvar();
            System.out.println("Projeto salvo com sucesso.");
            System.out.println("-------------------------");
        }
        else{
            System.out.println("Não foi possível salvar o projeto.");
            System.out.println("-------------------------");
        }
    }
    public static void opcaoAlterar() throws Exception{
        System.out.println("=== Alterar Projeto ===");
        System.out.print("Informe a id do projeto a ser alterado: ");
        int id = sc.nextInt();
        sc.nextLine();

        Projeto projeto = service.buscarPorId(id);
        if(projeto == null){
            System.out.println("Não foi possível encontrar o projeto.");
            System.out.println("-------------------------");
        }
        System.out.println("Dados Atuais:");
        projeto.exibirDados();
        System.out.println("-------------------------");

        System.out.println("Novos Dados:");
        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Descrição: ");
        String desc = sc.nextLine();

        System.out.print("Categoria: ");
        String cat = sc.nextLine();

        System.out.print("Status: ");
        String stts = sc.nextLine();

        Projeto projetoNovo = new Projeto(id, nome, desc, cat, stts);
        boolean alterado = service.alterarProjeto(projetoNovo);
        if(alterado){
            service.salvar();
            System.out.println("Projeto alterado.");
            System.out.println("-------------------------");
        }
        else{
            System.out.println("Não foi possível alterar o projeto.");
            System.out.println("-------------------------");
        }
    }
    public static void opcaoDeletar() throws Exception{
        System.out.println("=== Deletar Projeto ===");
        System.out.print("Informe a id do projeto a ser deletado: ");
        int id = sc.nextInt();
        sc.nextLine();

        Projeto projeto = service.buscarPorId(id);

        if(projeto == null){
            System.out.println("Não foi possível encontrar o projeto.");
            System.out.println("-------------------------");
        }
        System.out.println("Projeto que será excluído:");
        projeto.exibirDados();
        System.out.print("Confirma a exclusão? (S/N): ");
        String confirmacao = sc.nextLine();

        if(confirmacao.equalsIgnoreCase("S")){
            boolean removido = service.removerPorId(id);
            if(removido){
                service.salvar();
                System.out.println("Projeto excluído com sucesso.");
            }
            else{
                System.out.println("Exclusão cancelada.");
            }
        }
        System.out.println("-------------------------");
    }
    
}