import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Projeto;
import service.ProjetoService;
import dao.ProjetoCSV;

public class TelaProjetos extends JFrame{

    private JLabel labelNome;
    private JTextField campoNome;

    private JLabel labelDesc;
    private JTextField campoDesc;

    private JLabel labelCat;
    private JTextField campoCat;

    private JLabel labelStts;
    private JTextField campoStts;

    private JButton botaoCadastrar;
    private JButton botaoLimpar;
    private JButton botaoMostrar;

    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaProjetos(){
        setTitle("Sistema de Projetos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        labelNome = new JLabel("Nome:");
        campoNome = new JTextField(30);

        labelDesc = new JLabel("Descrição:");
        campoDesc = new JTextField(30);

        labelCat = new JLabel("Categoria:");
        campoCat = new JTextField(30);

        labelStts = new JLabel("Status:");
        campoStts = new JTextField(30);

        botaoCadastrar = new JButton("Cadastrar:");
        botaoMostrar = new JButton("MOSTRAR");

        JPanel painel = new JPanel();

        painel.add(labelNome);
        painel.add(campoNome);

        painel.add(labelDesc);
        painel.add(campoDesc);

        painel.add(labelCat);
        painel.add(campoCat);

        painel.add(labelStts);
        painel.add(campoStts);

        painel.add(botaoCadastrar);
        painel.add(botaoMostrar);
        add(painel);

        botaoCadastrar.addActionListener(e -> {
            String nome = campoNome.getText();
            System.out.println("Projeto: " + nome); 
        });

        JOptionPane.showMessageDialog(this,"Projeto Cadastrado!");

        modelo = new DefaultTableModel();

        modelo.addColumn("ID:");
        modelo.addColumn("Nome:");
        modelo.addColumn("Descrição:");
        modelo.addColumn("Categoria:");
        modelo.addColumn("Status:");
    }
    public static void main(String[]args){
        TelaProjetos tela = new TelaProjetos();
        tela.setVisible(true);
    }
}