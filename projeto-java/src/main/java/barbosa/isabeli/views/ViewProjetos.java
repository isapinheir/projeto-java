package barbosa.isabeli.views;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.components.NeoButton;
import barbosa.isabeli.components.NeoCard;
import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.utils.Fontes;
import barbosa.isabeli.utils.Cores;
import barbosa.isabeli.utils.Icones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ViewProjetos extends JPanel {

    // Cores principais
    private static final Color COR_FUNDO_PAGINA = new Color(248, 250, 252);
    private static final Color COR_BORDA = new Color(226, 232, 240);
    private static final Color COR_TEXTO_PRINCIPAL = new Color(30, 41, 59);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(71, 85, 105);

    private static final Color CARD_VERDE = Color.decode("#befb5e");
    private static final Color CARD_AMARELO = Color.decode("#fbd05e");
    private static final Color CARD_ROXO = Color.decode("#c38cf1");

    // Labels de métricas
    private JLabel lblMetricaTotal;
    private JLabel lblMetricaConcluido;
    private JLabel lblMetricaDesenvolvimento;
    private JLabel lblMetricaPlanejado;

    // Campos do formulário de cadastro
    private JLabel lblNome;
    private JTextField txtNome;
    private JLabel lblDesc;
    private JTextArea txtDesc;
    private JLabel lblCat;
    private JComboBox<String> cbCat;
    private JLabel lblStts;
    private JComboBox<String> cbStts;

    // Campos do modal de alteração
    private JLabel lblNomeNovo;
    private JTextField txtNomeNovo;
    private JLabel lblDescNova;
    private JTextArea txtDescNova;
    private JLabel lblCatNova;
    private JComboBox<String> cbCatNova;
    private JLabel lblSttsNovo;
    private JComboBox<String> cbSttsNovo;

    private int proximoId = 1;
    private List<Projeto> projetosCache = new ArrayList<>();

    // Tabela e filtro
    private JTextField txtBusca;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> filtro;
    private JTable tabelaProjetos;

    private BtnAcoes btnSalvar;
    private BtnAcoes btnLimpar;
    private BtnAcoes btnExcluir;
    private BtnAcoes btnAlterar;

    private ApiClient apiClient;

    // ========= ÍCONES =========
    private final ImageIcon iconePasta = Icones.get("folder-open.svg", 30, 30, Color.WHITE);
    private final ImageIcon iconeConfere = Icones.get("checkmark.svg", 30, 30, Color.WHITE);
    private final ImageIcon iconeRelogio = Icones.get("time.svg", 30, 30, Color.WHITE);
    private final ImageIcon iconeGrafico = Icones.get("bar-chart.svg", 30, 30, Color.WHITE);

    private final ImageIcon iconeSalvar = Icones.get("save.svg", 18, 18, Color.WHITE);
    private final ImageIcon iconeVoltar = Icones.get("return-down-back.svg", 18, 18, Color.WHITE);
    private final ImageIcon iconeLapis = Icones.get("pencil.svg", 18, 18, Color.WHITE);
    private final ImageIcon iconeLixo = Icones.get("trash.svg", 18, 18, Color.WHITE);

    private final ImageIcon iconeMais = Icones.get("add.svg", 20, 20, Color.WHITE);
    private final ImageIcon iconeLista = Icones.get("list.svg", 20, 20, Color.WHITE);

    //private final ImageIcon iconeLapis = Icones.get("pencil.svg", 18, 18, Color.WHITE);
    //private final ImageIcon iconeLixo = Icones.get("trash.svg", 18, 18, Color.WHITE);

    public ViewProjetos() {
        setLayout(new BorderLayout());
        this.apiClient = new ApiClient();

        JPanel paginaProjetos = new JPanel(new GridBagLayout());
        paginaProjetos.setBackground(COR_FUNDO_PAGINA);
        paginaProjetos.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // ========= GRID DO TOPO (MÉTRICAS) =========
        JPanel gridMetricas = new JPanel(new GridLayout(1, 4, 16, 0));
        gridMetricas.setOpaque(false);

        CardMetricas cardTotal = new CardMetricas(
                Cores.get("azul"),
                Cores.get("azulEscuro"),
                iconePasta,
                "0",
                "Total de Projetos"
        );
        CardMetricas cardConcluido = new CardMetricas(
                Cores.get("verde"),
                Cores.get("verdeEscuro"),
                iconeConfere,
                "0",
                "Concluídos"
        );
        CardMetricas cardDesenvolvimento = new CardMetricas(
                Cores.get("amarelo"),
                Cores.get("amareloEscuro"),
                iconeRelogio,
                "0",
                "Em desenvolvimento"
        );
        CardMetricas cardPlanejado = new CardMetricas(
                Cores.get("roxo"),
                Cores.get("roxoEscuro"),
                iconeGrafico,
                "0",
                "Planejados"
        );

        lblMetricaTotal = cardTotal.getLblValor();
        lblMetricaConcluido = cardConcluido.getLblValor();
        lblMetricaDesenvolvimento = cardDesenvolvimento.getLblValor();
        lblMetricaPlanejado = cardPlanejado.getLblValor();

        gridMetricas.add(cardTotal);
        gridMetricas.add(cardConcluido);
        gridMetricas.add(cardDesenvolvimento);
        gridMetricas.add(cardPlanejado);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 20, 0);
        paginaProjetos.add(gridMetricas, gbc);

        // ========= GRID DO MEIO (FORMULÁRIO + TABELA) =========
        JPanel gridConteudo = new JPanel(new GridBagLayout());
        gridConteudo.setOpaque(false);

        GridBagConstraints gbcConteudo = new GridBagConstraints();
        gbcConteudo.fill = GridBagConstraints.BOTH;
        gbcConteudo.gridy = 0;
        gbcConteudo.weighty = 1.0;

        // == Formulário ==
        gbcConteudo.gridx = 0;
        gbcConteudo.weightx = 0.35;
        gbcConteudo.insets = new Insets(0, 0, 0, 12);
        CardFormulario form = new CardFormulario(Cores.get("fundo"));
        gridConteudo.add(form, gbcConteudo);

        // == Tabela ==
        gbcConteudo.gridx = 1;
        gbcConteudo.weightx = 0.65;
        gbcConteudo.insets = new Insets(0, 0, 0, 0);
        CardTabela tabelaCard = new CardTabela(Cores.get("fundo"));
        gridConteudo.add(tabelaCard, gbcConteudo);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        paginaProjetos.add(gridConteudo, gbc);

        JScrollPane scrollPane = new JScrollPane(paginaProjetos);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        criarEventos();
        carregarPagina();
    }

    // ========= CARDS =========

    public static class CardMetricas extends NeoCard {

        private final JLabel lblValor;
        private final JLabel lblRotulo;

        public CardMetricas(Color corFundo, Color corIcone, ImageIcon icone, String valorInicial, String rotulo) {
            super(corFundo);

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(new EmptyBorder(14, 18, 20, 24));

            IconeCardMetricas wrapper = new IconeCardMetricas(corIcone, icone);
            wrapper.setPreferredSize(new Dimension(62, 62));
            wrapper.setMinimumSize(new Dimension(62, 62));
            wrapper.setMaximumSize(new Dimension(62, 62));

            JPanel textos = new JPanel();
            textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
            textos.setOpaque(false);

            lblValor = new JLabel(valorInicial);
            lblValor.setFont(Fontes.carregarFonte("Poppins-ExtraBold.ttf", 40f));
            lblValor.setForeground(Cores.get("textoPrincipal"));

            lblRotulo = new JLabel(rotulo);
            lblRotulo.setFont(Fontes.carregarFonte("Orbitron-Medium.ttf", 17f));
            lblRotulo.setForeground(Cores.get("textoSecundario"));

            textos.add(lblValor);
            textos.add(Box.createVerticalStrut(2));
            textos.add(lblRotulo);

            add(wrapper);
            add(Box.createHorizontalStrut(14));
            add(textos);
        }

        public void setValor(String valor) {
            lblValor.setText(valor);
        }

        public void setValor(int valor) {
            lblValor.setText(String.valueOf(valor));
        }

        public JLabel getLblValor() {
            return lblValor;
        }

        public JLabel getLblRotulo() {
            return lblRotulo;
        }

        static class IconeCardMetricas extends JPanel {
            private final Color corFundoIcone;

            IconeCardMetricas(Color corFundoIcone, ImageIcon icone) {
                this.corFundoIcone = corFundoIcone;
                setLayout(new BorderLayout());
                setOpaque(false);

                JLabel item = new JLabel();
                item.setHorizontalAlignment(SwingConstants.CENTER);
                item.setVerticalAlignment(SwingConstants.CENTER);
                item.setIcon(icone);

                add(item, BorderLayout.CENTER);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(corFundoIcone);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        }
    }

    // ========= CARD FORMULÁRIO =========

    public class CardFormulario extends NeoCard {

        public CardFormulario(Color corFundo) {
            super(corFundo);
            setLayout(new BorderLayout());

            JLabel cabecalho = new JLabel("Cadastro de Projetos");
            cabecalho.setFont(Fontes.carregarFonte("Orbitron-ExtraBold.ttf", 20f));
            cabecalho.setForeground(COR_TEXTO_PRINCIPAL);
            cabecalho.setIcon(iconeMais);
            cabecalho.setBorder(new EmptyBorder(18, 18, 0, 18));

            JPanel corpo = new JPanel();
            corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
            corpo.setOpaque(false);
            corpo.setBorder(new EmptyBorder(18, 18, 18, 18));

            // == Nome ==
            lblNome = new JLabel("Nome");
            estilizarLabel(lblNome);
            lblNome.setHorizontalAlignment(SwingConstants.LEFT);
            txtNome = new JTextField();
            estilizarTextField(txtNome);

            // == Descrição ==
            lblDesc = new JLabel("Descrição");
            lblDesc.setHorizontalAlignment(SwingConstants.LEFT);            
            estilizarLabel(lblDesc);

            txtDesc = new JTextArea("", 4, 20);
            txtDesc.setLineWrap(true);
            txtDesc.setWrapStyleWord(true);
            txtDesc.setFont(Fontes.carregarFonte("Orbitron-ExtraBold.ttf", 17f));
            txtDesc.setForeground(COR_TEXTO_PRINCIPAL);
            txtDesc.setBorder(new EmptyBorder(8, 10, 8, 10));

            JScrollPane scrollDesc = new JScrollPane(txtDesc);
            scrollDesc.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1, true));
            scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

            // == Categoria e Status (mesma linha) ==
            JPanel linhaDupla = new JPanel(new GridLayout(1, 2, 12, 0));
            linhaDupla.setOpaque(false);
            linhaDupla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

            JPanel colCategoria = new JPanel();
            colCategoria.setLayout(new BoxLayout(colCategoria, BoxLayout.Y_AXIS));
            colCategoria.setOpaque(false);

            lblCat = new JLabel("Categoria");
            lblCat.setHorizontalAlignment(SwingConstants.LEFT);
            estilizarLabel(lblCat);
        
            cbCat = new JComboBox<>(new String[]{"Software", "Web", "Mobile"});
            estilizarCombo(cbCat);

            JPanel colStatus = new JPanel();
            colStatus.setLayout(new BoxLayout(colStatus, BoxLayout.Y_AXIS));
            colStatus.setOpaque(false);

            lblStts = new JLabel("Status");
            lblStts.setHorizontalAlignment(SwingConstants.LEFT);
            estilizarLabel(lblStts);

            cbStts = new JComboBox<>(new String[]{"Em desenvolvimento", "Concluído", "Planejado"});
            estilizarCombo(cbStts);

            // == Botões Salvar e Limpar ==
            JPanel acoes = new JPanel(new GridLayout(1, 2, 12, 0));
            acoes.setOpaque(false);
            acoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            btnSalvar = new BtnAcoes("Salvar", Cores.get("statusSucesso"), iconeSalvar);
            btnLimpar = new BtnAcoes("Limpar", Cores.get("statusPrimario"), iconeVoltar);

            corpo.add(lblNome);
            corpo.add(Box.createVerticalStrut(6));
            corpo.add(txtNome);
            corpo.add(Box.createVerticalStrut(14));

            corpo.add(lblDesc);
            corpo.add(Box.createVerticalStrut(6));
            corpo.add(scrollDesc);
            corpo.add(Box.createVerticalStrut(14));

            colCategoria.add(lblCat);
            colCategoria.add(Box.createVerticalStrut(6));
            colCategoria.add(cbCat);

            colStatus.add(lblStts);
            colStatus.add(Box.createVerticalStrut(6));
            colStatus.add(cbStts);

            linhaDupla.add(colCategoria);
            linhaDupla.add(colStatus);
            corpo.add(linhaDupla);
            corpo.add(Box.createVerticalStrut(18));

            acoes.add(btnSalvar);
            acoes.add(btnLimpar);
            corpo.add(acoes);

            add(cabecalho, BorderLayout.NORTH);
            add(corpo, BorderLayout.CENTER);
        }
    }

    // ========= CARD TABELA =========

    public class CardTabela extends NeoCard {
        public CardTabela(Color corFundo) {
            super(corFundo);
            setLayout(new BorderLayout());

            JLabel cabecalho = new JLabel("Lista de Projetos");
            cabecalho.setFont(Fontes.carregarFonte("Orbitron-Bold.ttf", 20f));
            cabecalho.setForeground(COR_TEXTO_PRINCIPAL);
            cabecalho.setIcon(iconeLista);
            cabecalho.setBorder(new EmptyBorder(18, 18, 0, 18));

            JPanel corpo = new JPanel(new BorderLayout(0, 14));
            corpo.setOpaque(false);
            corpo.setBorder(new EmptyBorder(18, 18, 18, 18));

            JPanel barraPesquisa = new JPanel(new BorderLayout(8, 0));
            barraPesquisa.setOpaque(false);

            txtBusca = new JTextField();
            estilizarTextField(txtBusca);
            txtBusca.setToolTipText("Pesquisar por nome, categoria ou status...");
            txtBusca.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { filtrarTabela(); }
                @Override
                public void removeUpdate(DocumentEvent e) { filtrarTabela(); }
                @Override
                public void changedUpdate(DocumentEvent e) { filtrarTabela(); }
            });

            JButton btnAtualizar = new JButton();
            btnAtualizar.setBackground(Color.WHITE);
            btnAtualizar.setForeground(COR_TEXTO_SECUNDARIO);
            btnAtualizar.setFocusPainted(false);
            btnAtualizar.setIcon(iconeRelogio);
            btnAtualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAtualizar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA, 1, true),
                    new EmptyBorder(8, 14, 8, 14)
            ));
            btnAtualizar.addActionListener(e -> carregarPagina());

            // == Tabela ==
            modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            modelo.addColumn("Nome");
            modelo.addColumn("Descrição");
            modelo.addColumn("Categoria");
            modelo.addColumn("Status");

            filtro = new TableRowSorter<>(modelo);

            tabelaProjetos = new JTable(modelo);
            tabelaProjetos.setRowSorter(filtro);
            tabelaProjetos.setRowHeight(90);
            tabelaProjetos.setShowVerticalLines(false);
            tabelaProjetos.setGridColor(COR_BORDA);
            tabelaProjetos.setSelectionBackground(new Color(191, 155, 13, 52));
            tabelaProjetos.setSelectionForeground(COR_TEXTO_PRINCIPAL);
            tabelaProjetos.setFont(Fontes.carregarFonte("Poppins-Medium.ttf", 17f));

            JTableHeader header = tabelaProjetos.getTableHeader();
            header.setFont(Fontes.carregarFonte("Orbitron-ExtraBold.ttf", 17f));
            header.setBackground(new Color(241, 245, 249));
            header.setForeground(COR_TEXTO_PRINCIPAL);
            header.setReorderingAllowed(false);

            tabelaProjetos.getColumnModel().getColumn(0).setCellRenderer(new NegritoTabela());
            tabelaProjetos.getColumnModel().getColumn(2).setCellRenderer(new BadgeTabela(false));
            tabelaProjetos.getColumnModel().getColumn(3).setCellRenderer(new BadgeTabela(true));

            JScrollPane scrollTabela = new JScrollPane(tabelaProjetos);
            scrollTabela.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1, true));
            scrollTabela.getViewport().setBackground(Color.WHITE);

            barraPesquisa.add(txtBusca, BorderLayout.CENTER);
            barraPesquisa.add(btnAtualizar, BorderLayout.EAST);

            // == Rodapé ==
            JPanel rodapeAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            rodapeAcoes.setOpaque(false);

            btnAlterar = new BtnAcoes("Alterar", Cores.get("statusAlerta"), iconeLapis);
            btnExcluir = new BtnAcoes("Excluir", Cores.get("statusErro"), iconeLixo);

            rodapeAcoes.add(btnAlterar);
            rodapeAcoes.add(btnExcluir);

            corpo.add(barraPesquisa, BorderLayout.NORTH);
            corpo.add(scrollTabela, BorderLayout.CENTER);
            corpo.add(rodapeAcoes, BorderLayout.SOUTH);

            add(cabecalho, BorderLayout.NORTH);
            add(corpo, BorderLayout.CENTER);
        }
    }

    static class NegritoTabela extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setFont(Fontes.carregarFonte("Poppins-Bold.ttf", 17f));
            lbl.setForeground(COR_TEXTO_PRINCIPAL);
            return lbl;
        }
    }

    static class BadgeTabela extends DefaultTableCellRenderer {
        private final boolean isStatus;
        private final Font fonteBadge;

        BadgeTabela(boolean isStatus) {
            this.isStatus = isStatus;
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            // Carrega uma vez para não recarregar o arquivo a cada desenho da célula
            this.fonteBadge = Fontes.carregarFonte("Poppins-Regular.ttf", 14f);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            String texto = value != null ? value.toString() : "";
            String normalizado = normalizarTexto(texto);

            return new JPanel() {
                {
                    setOpaque(false);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    Color fundo;
                    Color borda = new Color(59, 59, 62);

                    if (!isStatus) {
                        fundo = new Color(241, 245, 249);
                    } else if ("concluido".equals(normalizado)) {
                        fundo = CARD_VERDE;
                    } else if ("em desenvolvimento".equals(normalizado)) {
                        fundo = CARD_AMARELO;
                    } else {
                        fundo = CARD_ROXO;
                    }

                    g2.setFont(fonteBadge);
                    FontMetrics fm = g2.getFontMetrics();

                    int paddingX = 14; 
                    int paddingY = 8; 
                    int margemLateral = 6;

                    int maxLarguraDisponivel = Math.max(20, getWidth() - (margemLateral * 2));
                    int pillW = Math.min(fm.stringWidth(texto) + (paddingX * 2), maxLarguraDisponivel);
                    int pillH = Math.min(fm.getHeight() + paddingY, Math.max(20, getHeight() - 8));

                    int px = (getWidth() - pillW) / 2;
                    int py = (getHeight() - pillH) / 2;

                    Shape pill = new RoundRectangle2D.Double(px, py, pillW, pillH, pillH, pillH);
                    g2.setColor(fundo);
                    g2.fill(pill);

                    if (isStatus) {
                        g2.setColor(borda);
                        g2.draw(pill);
                    }

                    g2.setColor(COR_TEXTO_SECUNDARIO);
                    int textX = px + ((pillW - fm.stringWidth(texto)) / 2);
                    int textY = py + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(texto, textX, textY);

                    g2.dispose();
                }
            };
        }
    }

    // ========= BOTÕES =========

    public static class BtnAcoes extends NeoButton {
        public BtnAcoes(String txt, Color cor, ImageIcon icone) {
            super(txt, cor);
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setIcon(icone);
        }
    }

    // ========= POPUP DE ALTERAR PROJETO =========

    private JPanel criarPainelAlterar(Projeto projetoAtual) {
        JPanel painelAlterar = new JPanel();
        painelAlterar.setLayout(new BoxLayout(painelAlterar, BoxLayout.Y_AXIS));
        painelAlterar.setOpaque(false);
        painelAlterar.setBorder(new EmptyBorder(12, 12, 12, 12));

        lblNomeNovo = new JLabel("Nome");
        estilizarLabel(lblNomeNovo);
        txtNomeNovo = new JTextField(projetoAtual.getNome());
        estilizarTextField(txtNomeNovo);

        lblDescNova = new JLabel("Descrição");
        estilizarLabel(lblDescNova);

        txtDescNova = new JTextArea(projetoAtual.getDescricao(), 4, 24);
        txtDescNova.setLineWrap(true);
        txtDescNova.setWrapStyleWord(true);
        txtDescNova.setFont(Fontes.carregarFonte("Poppins-Medium.ttf", 17f));
        txtDescNova.setForeground(COR_TEXTO_PRINCIPAL);
        txtDescNova.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane scrollDescNovo = new JScrollPane(txtDescNova);
        scrollDescNovo.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1, true));
        scrollDescNovo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        JPanel linhaDupla = new JPanel(new GridLayout(1, 2, 12, 0));
        linhaDupla.setOpaque(false);

        JPanel colCategoria = new JPanel();
        colCategoria.setLayout(new BoxLayout(colCategoria, BoxLayout.Y_AXIS));
        colCategoria.setOpaque(false);

        lblCatNova = new JLabel("Categoria");
        estilizarLabel(lblCatNova);
        cbCatNova = new JComboBox<>(new String[]{"Software", "Web", "Mobile"});
        cbCatNova.setSelectedItem(projetoAtual.getCategoria());
        estilizarCombo(cbCatNova);

        JPanel colStatus = new JPanel();
        colStatus.setLayout(new BoxLayout(colStatus, BoxLayout.Y_AXIS));
        colStatus.setOpaque(false);

        lblSttsNovo = new JLabel("Status");
        estilizarLabel(lblSttsNovo);
        cbSttsNovo = new JComboBox<>(new String[]{"Em desenvolvimento", "Concluído", "Planejado"});
        cbSttsNovo.setSelectedItem(projetoAtual.getStatus());
        estilizarCombo(cbSttsNovo);

        colCategoria.add(lblCatNova);
        colCategoria.add(Box.createVerticalStrut(6));
        colCategoria.add(cbCatNova);

        colStatus.add(lblSttsNovo);
        colStatus.add(Box.createVerticalStrut(6));
        colStatus.add(cbSttsNovo);

        linhaDupla.add(colCategoria);
        linhaDupla.add(colStatus);

        painelAlterar.add(lblNomeNovo);
        painelAlterar.add(Box.createVerticalStrut(6));
        painelAlterar.add(txtNomeNovo);
        painelAlterar.add(Box.createVerticalStrut(12));
        painelAlterar.add(lblDescNova);
        painelAlterar.add(Box.createVerticalStrut(6));
        painelAlterar.add(scrollDescNovo);
        painelAlterar.add(Box.createVerticalStrut(12));
        painelAlterar.add(linhaDupla);

        return painelAlterar;
    }

    // ========= CRUD =========

    private void cadastrar() {
        try {
        	int id = proximoId;
            String nome = txtNome.getText().trim();
            String desc = txtDesc.getText().trim();
            String cat = String.valueOf(cbCat.getSelectedItem());
            String stts = String.valueOf(cbStts.getSelectedItem());

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do projeto.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a descrição do projeto.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Projeto projeto = new Projeto(id, nome, desc, cat, stts);
            apiClient.cadastrarProjeto(projeto);
            apiClient.registrarLogProjeto("CRIAR", "ID=" + id + " | Nome=" + nome);

            JOptionPane.showMessageDialog(this, "Projeto cadastrado com sucesso!");
            limparFormulario();
            carregarPagina();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            int linhaView = tabelaProjetos.getSelectedRow();
            if (linhaView == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um projeto.");
                return;
            }

            int linhaModel = tabelaProjetos.convertRowIndexToModel(linhaView);
            int id = (int) modelo.getValueAt(linhaModel, 0);

            Projeto atual = projetosCache.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(new Projeto(
                            id,
                            String.valueOf(modelo.getValueAt(linhaModel, 1)),
                            "",
                            String.valueOf(modelo.getValueAt(linhaModel, 2)),
                            String.valueOf(modelo.getValueAt(linhaModel, 3))
                    ));

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    criarPainelAlterar(atual),
                    "Alterar Projeto",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcao != JOptionPane.OK_OPTION) {
                return;
            }

            String nome = txtNomeNovo.getText().trim();
            String descricao = txtDescNova.getText().trim();
            String categoria = String.valueOf(cbCatNova.getSelectedItem());
            String status = String.valueOf(cbSttsNovo.getSelectedItem());

            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "Informe o nome.");
                return;
            }

            Projeto novoProjeto = new Projeto(id, nome, descricao, categoria, status);
            apiClient.alterarProjeto(id, novoProjeto);
            apiClient.registrarLogProjeto("ALTERAR", "ID=" + id + " | Nome=" + nome + " | Status=" + status);
            carregarPagina();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        try {
            int linhaView = tabelaProjetos.getSelectedRow();
            if (linhaView == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um projeto.");
                return;
            }

            int linhaModel = tabelaProjetos.convertRowIndexToModel(linhaView);
            int id = (int) modelo.getValueAt(linhaModel, 0);
            apiClient.excluirProjeto(id);
            apiClient.registrarLogProjeto("EXCLUIR", "ID=" + id);
            carregarPagina();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularProximoId(List<Projeto> projetos) {
        int maior = 0;
        if (projetos != null) {
            for (Projeto projeto : projetos) {
                if (projeto.getId() > maior) {
                    maior = projeto.getId();
                }
            }
        }
        this.proximoId = maior + 1;
    }

    // ========= LÓGICA =========

    private void criarEventos() {
        btnLimpar.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> cadastrar());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtDesc.setText("");
        cbCat.setSelectedIndex(0);
        cbStts.setSelectedIndex(0);
        txtNome.requestFocus();
    }

    private void carregarPagina() {
        new SwingWorker<List<Projeto>, Void>() {

            @Override
            protected List<Projeto> doInBackground() throws Exception {
                return apiClient.listarProjetos();
            }

            @Override
            protected void done() {
                try {
                    List<Projeto> lista = get();
                    projetosCache = lista != null ? lista : new ArrayList<>();
                    atualizarTabela(projetosCache);
                    atualizarCards(projetosCache);
                    calcularProximoId(projetosCache);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            ViewProjetos.this,
                            "Erro ao carregar: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.execute();
    }

    private void filtrarTabela() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            filtro.setRowFilter(null);
        } else {
            filtro.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(termo)));
        }
    }

    private void atualizarTabela(List<Projeto> projetos) {
        modelo.setRowCount(0);

        for (Projeto projeto : projetos) {
            modelo.addRow(new Object[]{
                    projeto.getNome(),
                    projeto.getDescricao(),
                    projeto.getCategoria(),
                    projeto.getStatus()
            });
        }
    }

    private void atualizarCards(List<Projeto> projetos) {
        int total = 0;
        int concluidos = 0;
        int desenvolvimento = 0;
        int planejados = 0;

        for (Projeto projeto : projetos) {
            String status = normalizarTexto(projeto.getStatus());

            switch (status) {
                case "concluido":
                    concluidos++;
                    break;
                case "em desenvolvimento":
                    desenvolvimento++;
                    break;
                case "planejado":
                    planejados++;
                    break;
                default:
                    System.err.println("Projeto com status fora do padrão: " + projeto);
                    break;
            }
            total++;
        }

        lblMetricaTotal.setText(String.valueOf(total));
        lblMetricaConcluido.setText(String.valueOf(concluidos));
        lblMetricaDesenvolvimento.setText(String.valueOf(desenvolvimento));
        lblMetricaPlanejado.setText(String.valueOf(planejados));
    }

    private static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.trim().toLowerCase();
    }

    // ========= MÉTODOS DE ESTILIZAÇÃO =========

    private void estilizarLabel(JLabel lbl) {
        lbl.setFont(Fontes.carregarFonte("Poppins-Bold.ttf", 17f));
        lbl.setForeground(COR_TEXTO_PRINCIPAL);
    }

    private void estilizarTextField(JTextField txt) {
        txt.setFont(Fontes.carregarFonte("Poppins-Medium.ttf", 17f));
        txt.setForeground(COR_TEXTO_PRINCIPAL);
        txt.setBackground(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(Fontes.carregarFonte("Poppins-Medium.ttf", 17f));
        combo.setForeground(COR_TEXTO_PRINCIPAL);
        combo.setBackground(Color.WHITE);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }
}