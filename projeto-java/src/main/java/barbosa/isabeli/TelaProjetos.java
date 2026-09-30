package barbosa.isabeli;

import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.dao.ProjetoCSV;
import barbosa.isabeli.service.ProjetoService;

import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaProjetos extends JFrame {

    private JTextField campoId;
    private JTextField campoNome;
    private JTextField campoDescricao;

    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;

    static ProjetoService service = new ProjetoService();
    static ProjetoCSV dao = new ProjetoCSV();

	    public TelaProjetos() throws Exception{

	        setTitle("Sistema de Projetos");
	        setSize(800, 500);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

	        modelo = new DefaultTableModel();
	        modelo.addColumn("ID");
	        modelo.addColumn("Nome");
	        modelo.addColumn("Categoria");
	        modelo.addColumn("Status");
	        tabela = new JTable(modelo);

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

	        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

	        add(painelFormulario);
	        add(new JScrollPane(tabela));
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
	        for (Projeto projeto : service.listar()) {
	            modelo.addRow(new Object[]{
	                projeto.getId(),
	                projeto.getNome(),
	                projeto.getCategoria(),
	                projeto.getStatus()
	            });
	        }
	    }

	    private void cadastrar() throws Exception{
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

	        service.adicionar(projeto);
	        service.salvar();

	        JOptionPane.showMessageDialog(this,"Projeto cadastrado com sucesso!");

	        limparFormulario();
	        carregarTabela();
	    }        

	    public static void main(String[] args) throws Exception{
	        service.carregar();
	        TelaProjetos tela = new TelaProjetos();
	        tela.setVisible(true);
	    }
}