
import model.Projeto;

public class Main {

    public static void main(String[] args) {

        Projeto projeto1 = new Projeto(
            1,
            "Sistema Acadêmico",
            "Software para gerenciamento acadêmico",
            "Software",
            "Em desenvolvimento"
        );

        Projeto projeto2 = new Projeto(
            2,
            "Site Institucional",
            "Site de uma instituição",
            "Web",
            "Concluído"
        );

        System.out.println(projeto1.getId());
        System.out.println(projeto1.getNome());
        System.out.println(projeto1.getDescricao());
        System.out.println(projeto1.getCategoria());
        System.out.println(projeto1.getStatus());

        System.out.println();

        System.out.println(projeto2.getId());
        System.out.println(projeto2.getNome());
        System.out.println(projeto2.getDescricao());
        System.out.println(projeto2.getCategoria());
        System.out.println(projeto2.getStatus());

    }
}
