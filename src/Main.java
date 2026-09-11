
import model.Projeto;

public class Main {

    public static void main(String[] args) {

        Projeto projeto1 = new Projeto();
        Projeto projeto2 = new Projeto();
        Projeto projeto3 = new Projeto();

        projeto1.setId(1);
        projeto1.setNome("Sistema Acadêmico");
        projeto1.setDescricao("Software para gerenciamento acadêmico");
        projeto1.setCategoria("Software");
        projeto1.setStatus("Em desenvolvimento");

        System.out.println("===========================");
        System.out.println("=== SISTEMA DE PROJETOS ===");
        System.out.println("===========================");
        System.out.println("= Bem-vindo(a) ao sistema!=");

        System.out.println(projeto1.getId());
        System.out.println(projeto1.getNome());
        System.out.println(projeto1.getDescricao());
        System.out.println(projeto1.getCategoria());
        System.out.println(projeto1.getStatus());

    }
}
