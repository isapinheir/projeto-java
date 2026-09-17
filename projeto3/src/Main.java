
import java.util.ArrayList;
import model.Projeto;

public class Main {

    public static void main(String[] args) {

        ArrayList<Projeto> projetos = new ArrayList<>();

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

        projetos.add(projeto1);
        projetos.add(projeto2);

        for(Projeto projeto : projetos){
            projeto.exibirDados();
        }
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