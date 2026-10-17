package barbosa.isabeli;

import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.api.Api;
import barbosa.isabeli.dao.ProjetoCSV;
import barbosa.isabeli.client.ApiClient;

import io.javalin.Javalin;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaProjetos extends JFrame {

    private JTextField campoId;
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoNomeNovo;
    private JTextField campoDescricaoNova;

    private JComboBox<String> comboCategoriaNova;
    private JComboBox<String> comboStatusNovo;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoAlterar;
    private JButton botaoExcluir;

    static ApiClient apiClient;
    static ProjetoCSV dao = new ProjetoCSV();

    public TelaProjetos() throws Exception{

        setTitle("Sistema de Projetos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        apiClient = new ApiClient();
        criarComponentes();
        criarEventos();
        carregarTabela();
    }

    private void criarComponentes() throws Exception {
        campoId = new JTextField(2);
        campoNome = new JTextField(20);
        campoDescricao = new JTextField(20);

        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Web");
        comboCategoria.addItem("Software");
        comboCategoria.addItem("Mobile");
        comboCategoria.addItem("Outro");

        comboStatus = new JComboBox<>();
        comboStatus.addItem("Planejado");
        comboStatus.addItem("Em desenvolvimento");
        comboStatus.addItem("Concluído");

        botaoCadastrar = new JButton("Cadastrar");
        botaoLimpar = new JButton("Limpar");
        botaoAlterar = new JButton("Alterar");
        botaoExcluir = new JButton("Excluir");

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nome");
        modelo.addColumn("Categoria");
        modelo.addColumn("Status");
        tabela = new JTable(modelo);
        
        campoNomeNovo = new JTextField(20);
        campoDescricaoNova = new JTextField(20);

        comboCategoriaNova = new JComboBox<>();
        comboCategoriaNova.addItem("Web");
        comboCategoriaNova.addItem("Software");
        comboCategoriaNova.addItem("Mobile");
        comboCategoriaNova.addItem("Outro");

        comboStatusNovo = new JComboBox<>();
        comboStatusNovo.addItem("Planejado");
        comboStatusNovo.addItem("Em desenvolvimento");
        comboStatusNovo.addItem("Concluído");

        JPanel painelFormulario = new JPanel();

        painelFormulario.add(new JLabel("Id:"));
        painelFormulario.add(campoId);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(campoDescricao);

        painelFormulario.add(new JLabel("Categoria:"));
        painelFormulario.add(comboCategoria);

        painelFormulario.add(new JLabel("Status:"));
        painelFormulario.add(comboStatus);

        painelFormulario.add(botaoCadastrar);
        painelFormulario.add(botaoLimpar);
        painelFormulario.add(botaoAlterar);
        painelFormulario.add(botaoExcluir);

        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

        add(painelFormulario);
        add(new JScrollPane(tabela));
    }
    
    private JPanel criarPainelAlterar() {
    	JPanel painelAlterar = new JPanel();
        
        painelAlterar.add(new JLabel("Nome:"));
        painelAlterar.add(campoNomeNovo);

        painelAlterar.add(new JLabel("Descrição:"));
        painelAlterar.add(campoDescricaoNova);

        painelAlterar.add(new JLabel("Categoria:"));
        painelAlterar.add(comboCategoriaNova);

        painelAlterar.add(new JLabel("Status:"));
        painelAlterar.add(comboStatusNovo);
        
        return painelAlterar;
    }

    private void criarEventos() {
        botaoLimpar.addActionListener(e -> {
            try{
                limparFormulario();
            }
            catch (Exception ex){
                System.out.println("Falha ao limpar formulário: " + ex.getMessage());
            }
        });
        botaoCadastrar.addActionListener(e -> {
            try {
                cadastrar();
            } catch (Exception ex) {
                System.out.println("Falha no cadastro: " + ex.getMessage());
            }
        });
        botaoAlterar.addActionListener(e ->{
        	try {
        		alterar();
        	}
        	catch(Exception ex){
        		System.out.println("Falha ao alterar: " + ex.getMessage());
        	}
        });
        botaoExcluir.addActionListener(e -> {
            try {
                excluir();
            } catch (Exception ex) {
                System.out.println("Falha ao apagar: " + ex.getMessage());
            }
        });
    }

    private void limparFormulario() throws Exception{
        campoNome.setText("");
        campoDescricao.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        campoNome.requestFocus();
    }

    private void carregarTabela() throws Exception{
        modelo.setRowCount(0);
        List<Projeto> projetos = apiClient.listarProjetos();
        for (Projeto projeto : projetos) {
            modelo.addRow(new Object[]{
                projeto.getId(),
                projeto.getNome(),
                projeto.getCategoria(),
                projeto.getStatus()
            });
        }
    }

    private void cadastrar() throws Exception{
    	try {
	        int id = Integer.parseInt(campoId.getText());
	        String nome = campoNome.getText();
	        String descricao = campoDescricao.getText();
	        String categoria = comboCategoria.getSelectedItem().toString();
	        String status = comboStatus.getSelectedItem().toString();
	
	        if (nome.isBlank()) {
	            JOptionPane.showMessageDialog(this,"Informe o nome.");
	            return;
	        }
	        Projeto projeto = new Projeto(id, nome, descricao, categoria, status);
	
	        apiClient.cadastrarProjeto(projeto);
	
	        JOptionPane.showMessageDialog(this,"Projeto cadastrado com sucesso!");
	
	        limparFormulario();
	        carregarTabela();
    	}
    	catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    	}
    }
    private void excluir() throws Exception{
    	try {
	        int linha = tabela.getSelectedRow();
	        if(linha == -1) {
	        	JOptionPane.showMessageDialog(this, "Selecione um projeto.");
	        	return;
	        }
	        
	        int id = (int) modelo.getValueAt(linha, 0);
	        apiClient.excluirProjeto(id);
	        carregarTabela();
    	}
    	catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    	}
    }
    private void alterar() throws Exception{
    	try {
	        int linha = tabela.getSelectedRow();
	        if(linha == -1) {
	        	JOptionPane.showMessageDialog(this, "Selecione um projeto.");
	        	return;
	        }
	        JOptionPane.showMessageDialog(null, criarPainelAlterar());
	        
	        int id = (int) modelo.getValueAt(linha, 0);
	        String nome = campoNomeNovo.getText();
	        String descricao = campoDescricaoNova.getText();
	        String categoria = comboCategoriaNova.getSelectedItem().toString();
	        String status = comboStatusNovo.getSelectedItem().toString();
	
	        if (nome.isBlank()) {
	            JOptionPane.showMessageDialog(this,"Informe o nome.");
	            return;
	        }
	        Projeto novoProjeto = new Projeto(id, nome, descricao, categoria, status);
	        apiClient.alterarProjeto(id, novoProjeto);
	        carregarTabela();
    	}
    	catch(Exception e){
    		JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    	}
    }

    public static void main(String[] args) throws Exception{
        Javalin app = Javalin.create(config ->{
        	config.bundledPlugins.enableCors(cors ->{
        		cors.addRule(it -> {
        			it.anyHost();
        		});
        	});
        }).start(7000);
        Api.web(app);
        TelaProjetos tela = new TelaProjetos();
        tela.setVisible(true);
    }
}