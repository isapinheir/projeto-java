package barbosa.isabeli.dao;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

import barbosa.isabeli.model.Projeto;

public class ProjetoCSV {
    private Path caminho;

    public ProjetoCSV(){
        caminho = Path.of("projetos.csv");
    }

    public void salvar(List<Projeto> projetos) throws Exception{
        List<String> linhas = new ArrayList<>();
        linhas.add("id;nome;descricao;categoria;status");

        for(Projeto projeto : projetos){
            String linha = 
                projeto.getId() + ";" +
                projeto.getNome() + ";" +
                projeto.getDescricao() + ";" +
                projeto.getCategoria() + ";" +
                projeto.getStatus();
            linhas.add(linha);
        }
        Files.write(caminho, linhas);
    } // todo o arquivo precisa ser regravado...

    public List<Projeto> listar() throws Exception {
        List<Projeto> projetos = new ArrayList<>();
        Path caminhoArquivo = Path.of("projetos.csv");

        if (!Files.exists(caminhoArquivo)) {
            System.out.println("O arquivo de dados não existe.");
            return projetos;
        }

        List<String> linhas = Files.readAllLines(caminhoArquivo, StandardCharsets.UTF_8);

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) continue;

            if (i == 1 && linha.startsWith("\uFEFF")) {
                linha = linha.substring(1);
            }

            String[] dados = linha.split(";", -1);

            if (dados.length >= 5) {
                try {
                    // .trim() remove espaços antes/depois do número
                    int id = Integer.parseInt(dados[0].trim()); 
                    String nome = dados[1].trim();
                    String descricao = dados[2].trim();
                    String categoria = dados[3].trim();
                    String status = dados[4].trim();

                    Projeto projeto = new Projeto(id, nome, descricao, categoria, status);
                    projetos.add(projeto);

                } catch (NumberFormatException e) {
                    System.err.println("ERRO na linha " + (i + 1) + ": Não foi possível converter o ID '" + dados[0] + "' para número.");
                } catch (Exception e) {
                    System.err.println("ERRO inesperado na linha " + (i + 1) + ": " + e.getMessage());
                }
            }
        }
        return projetos;
    }
    }
 // não permite multiplos acessos
// precisa salvar toda hora