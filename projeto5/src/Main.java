import model.Projeto;
import dao.ProjetoCSV;
import service.ProjetoService;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    // constantes para adicionar cores na interface
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static ProjetoService service = new ProjetoService();
    static ProjetoCSV dao = new ProjetoCSV();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        int opt, opt2;

        service.carregar();
        System.out.println(ANSI_BLUE + "============================");
        System.out.println("=== PORTIFÓLIO ACADÊMICO ===");
        System.out.println("============================");
        System.out.println("= Bem-vindo(a) ao sistema! =");
        System.out.println("");
        System.out.println(ANSI_YELLOW + "Total de projetos: " + ANSI_RESET + service.listar().size());
        System.out.println("");

        do{
            System.out.println(ANSI_YELLOW + "1-"+ ANSI_RESET + "Listar");
            System.out.println(ANSI_YELLOW + "2-"+ ANSI_RESET + "Buscar");
            System.out.println(ANSI_YELLOW + "3-"+ ANSI_RESET + "Cadastrar");
            System.out.println(ANSI_YELLOW + "4-"+ ANSI_RESET + "Alterar");
            System.out.println(ANSI_YELLOW + "5-"+ ANSI_RESET + "Excluir");
            System.out.println(ANSI_YELLOW + "0-"+ ANSI_RESET + "Sair");

            System.out.print("Escolha --> ");
            opt = sc.nextInt();

            switch(opt){
                case 1:
                    opcaoListar();
                    break;
                case 2:
                    System.out.println(ANSI_BLUE + "=== Busca ===" + ANSI_RESET);
                    System.out.println("Como deseja realizar sua busca?");
                    System.out.println("1- Por id");
                    System.out.println("2- Por nome");
                    System.out.println("3- Por descrição");
                    System.out.println("4- Por categoria");
                    System.out.println("5- Por status");

                    System.out.print("Escolha --> ");
                    opt2 = sc.nextInt();

                    switch(opt2){
                        case 1:
                            opcaoBuscaId();
                            break;
                        case 2:
                            opcaoBuscaNome();
                            break;
                        case 3:
                            opcaoBuscaDescricao();
                            break;
                        case 4:
                            opcaoBuscaCategoria();
                            break;
                        case 5:
                            opcaoBuscaStatus();
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
                    System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
        while(opt != 0);   
    }

    public static void opcaoListar(){
        System.out.println(ANSI_BLUE + "=== Projetos ===" + ANSI_RESET);
        for(Projeto projeto : service.listar()){
            projeto.exibirDados();
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
    }
    public static void opcaoBuscaId(){
        System.out.print("Digite o id do projeto que deseja buscar: ");
        int busca = sc.nextInt();
        System.out.println("");

        Projeto projeto = service.buscarPorId(busca);
        if(projeto != null){
            projeto.exibirDados(); 
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);       
        }
        else{
            System.out.println("Não foi possível buscar o projeto.");
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
    }
    public static void opcaoBuscaCategoria(){
        System.out.print("Digite a categoria do projeto que deseja buscar: ");
        sc.nextLine();
        String cat = sc.nextLine();

        for(Projeto projeto : service.buscarPorCategoria(cat)){
            projeto.exibirDados();
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);    
        }

        System.out.println("Total de projetos da categoria: " + service.buscarPorCategoria(cat).size());
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
    }
    public static void opcaoBuscaNome(){
        System.out.print("Digite o nome do projeto que deseja buscar: ");
        sc.nextLine();
        String nome = sc.nextLine();

        for(Projeto projeto : service.buscarPorNome(nome)){
            projeto.exibirDados();
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        System.out.println("Total de projetos com nome correspondente: " + service.buscarPorNome(nome).size());
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
    }
    public static void opcaoBuscaDescricao(){
        System.out.print("Digite a descrição do projeto que deseja buscar: ");
        String desc = sc.nextLine();
        sc.nextLine();

        for(Projeto projeto : service.buscarPorDescricao(desc)){
            projeto.exibirDados();
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        System.out.println("Total de projetos com descrição correspondente: " + service.buscarPorDescricao(desc).size());
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
    }
    public static void opcaoBuscaStatus(){
        System.out.print("Digite o status do projeto que deseja buscar: ");
        String stts = sc.nextLine();
        sc.nextLine();

        for(Projeto projeto : service.buscarPorStatus(stts)){
            projeto.exibirDados();
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        System.out.println("Total de projetos com o status selecionado: " + service.buscarPorStatus(stts).size());
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
    }
    public static void opcaoCadastro() throws Exception{
        System.out.println(ANSI_BLUE + "=== Cadastro ===" + ANSI_RESET);

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
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        else{
            System.out.println("Não foi possível salvar o projeto.");
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
    }
    public static void opcaoAlterar() throws Exception{
        System.out.println(ANSI_BLUE + "=== Alterar Projeto ===" + ANSI_RESET);
        System.out.print("Informe a id do projeto a ser alterado: ");
        int id = sc.nextInt();
        sc.nextLine();

        Projeto projeto = service.buscarPorId(id);
        if(projeto == null){
            System.out.println("Não foi possível encontrar o projeto.");
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        System.out.println("Dados Atuais:");
        projeto.exibirDados();
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);

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
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
        else{
            System.out.println("Não foi possível alterar o projeto.");
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
        }
    }
    public static void opcaoDeletar() throws Exception{
        System.out.println(ANSI_BLUE + "=== Deletar Projeto ===" + ANSI_RESET);
        System.out.print("Informe a id do projeto a ser deletado: ");
        int id = sc.nextInt();
        sc.nextLine();

        Projeto projeto = service.buscarPorId(id);

        if(projeto == null){
            System.out.println("Não foi possível encontrar o projeto.");
            System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
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
        System.out.println(ANSI_YELLOW + "-------------------------" + ANSI_RESET);
    }
    
}