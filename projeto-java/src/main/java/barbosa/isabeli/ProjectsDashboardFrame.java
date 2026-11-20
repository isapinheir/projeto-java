package barbosa.isabeli;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProjectsDashboardFrame extends JFrame {

    public ProjectsDashboardFrame() {
        // Cor de fundo geral off-white (#F4F0EA ou similar)
        getContentPane().setBackground(new Color(0xF4, 0xF0, 0xEA));
        setLayout(new BorderLayout());

        // ==========================================
        // 1. MENU LATERAL (ESQUERDA)
        // ==========================================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0xF4, 0xE6, 0xCF)); // --cor-sidebar-fundo
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel logoLabel = new JLabel("<html><b>Sistema</b><br>de Projetos</html>");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createSidebarItem("📊 Dashboard", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createSidebarItem("📁 Projetos", true)); // Ativo (roxo)
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createSidebarItem("≡ Categorias", false));

        add(sidebar, BorderLayout.WEST);

        // ==========================================
        // 2. PAINEL PRINCIPAL (DIREITA)
        // ==========================================
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 2.1 Topo (Data e Perfil) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        
        JLabel dateLabel = new JLabel("Quarta-feira, 10 de novembro de 2026, 14:44");
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Simulação do badge de usuário à direita
        JPanel userBadge = new NeoBox(Color.WHITE, 2);
        userBadge.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel userLabel = new JLabel("👤 João Silva");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        userBadge.add(userLabel);

        topBar.add(dateLabel, BorderLayout.WEST);
        topBar.add(userBadge, BorderLayout.EAST);
        mainContent.add(topBar, BorderLayout.NORTH);

        // --- 2.2 Centro (Métricas + Formulário/Tabela) ---
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // Linha de Cards de Métricas (topo do centro)
        JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        metricsPanel.setOpaque(false);
        metricsPanel.setPreferredSize(new Dimension(getWidth(), 80));

        metricsPanel.add(new MetricCard("11", "Total de Projetos", new Color(0x89, 0xC0, 0xF4))); // Azul
        metricsPanel.add(new MetricCard("3", "Concluídos", new Color(0xBE, 0xFB, 0x5E)));        // Verde
        metricsPanel.add(new MetricCard("6", "Em desenvolvimento", new Color(0xFB, 0xD0, 0x5E))); // Amarelo
        metricsPanel.add(new MetricCard("2", "Planejados", new Color(0xC3, 0x8C, 0xF1)));        // Roxo

        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        // Seção Inferior: Esquerda (Formulário) e Direita (Tabela)
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        splitPanel.setOpaque(false);

        splitPanel.add(createFormCard());
        splitPanel.add(createTableCard());

        centerPanel.add(splitPanel, BorderLayout.CENTER);
        mainContent.add(centerPanel, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);
    }

    /** Helper para criar os itens do menu lateral */
    private JComponent createSidebarItem(String text, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        item.setMaximumSize(new Dimension(190, 40));
        item.setOpaque(active);
        if (active) {
            item.setBackground(new Color(0x9B, 0x5E, 0xFB)); // --cor-sidebar-ativo
            item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        } else {
            item.setOpaque(false);
        }
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(active ? Color.WHITE : Color.BLACK);
        item.add(lbl);
        return item;
    }

    /** Componente de Card de Métrica Neobrutalista */
    static class MetricCard extends JPanel {
        public MetricCard(String number, String label, Color bg) {
            setLayout(new BorderLayout());
            setBackground(bg);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 3),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            
            JLabel numLbl = new JLabel(number);
            numLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
            
            JLabel textLbl = new JLabel(label);
            textLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));

            add(numLbl, BorderLayout.CENTER);
            add(textLbl, BorderLayout.SOUTH);
        }
    }

    /** Painel genérico com borda preta grossa para simular blocos */
    static class NeoBox extends JPanel {
        public NeoBox(Color bg, int borderThickness) {
            setBackground(bg);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, borderThickness));
        }
    }

    /** Cria o Card esquerdo de Cadastro de Projeto */
    private JPanel createFormCard() {
        JPanel card = new NeoBox(Color.WHITE, 3);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("+ Cadastro de Projeto");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(title, gbc);

        gbc.gridy++;
        card.add(new JLabel("Nome do Projeto"), gbc);
        gbc.gridy++;
        card.add(new JTextField("Sistema Academico"), gbc);

        gbc.gridy++;
        card.add(new JLabel("Descrição"), gbc);
        gbc.gridy++;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setText("Sistema para gerenciamento acadêmico");
        descArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        card.add(new JScrollPane(descArea), gbc);

        gbc.gridy++;
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(0x12, 0xC8, 0x54));
        btnSalvar.setForeground(Color.WHITE);
        
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(0x4C, 0x65, 0xEF));
        btnLimpar.setForeground(Color.WHITE);

        btnPanel.add(btnSalvar);
        btnPanel.add(btnLimpar);
        card.add(btnPanel, gbc);

        return card;
    }

    /** Cria o Card direito de Lista de Projetos */
    private JPanel createTableCard() {
        JPanel card = new NeoBox(Color.WHITE, 3);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("≡ Lista de Projetos");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(title, BorderLayout.NORTH);

        // Tabela simulada
        String[] columns = {"Nome do Projeto", "Categoria", "Status"};
        Object[][] data = {
            {"Sistema Acadêmico", "Software", "Em desenvolvimento"},
            {"Site Institucional", "Web", "Concluído"},
            {"Aplicativo Mobile", "Mobile", "Planejado"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProjectsDashboardFrame().setVisible(true);
        });
    }
}