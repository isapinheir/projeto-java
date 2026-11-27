package barbosa.isabeli.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.model.Projeto;
import barbosa.isabeli.utils.Textos;

public class ViewCategorias extends JPanel {

    // Cores do #paginaCategorias e .card-categoria
    private static final Color COR_FUNDO_PAGINA = new Color(248, 250, 252);
    private static final Color COR_TEXTO_ESCURO = new Color(30, 41, 59);
    private static final Color COR_TEXTO_CLARO = Color.WHITE;
    private static final Color COR_BORDA = new Color(203, 213, 225);

    // Azul (Software)
    private static final Color AZUL_FUNDO = Color.decode("#89c0f4");
    private static final Color AZUL_HEADER = Color.decode("#0d52bf");

    // Rosa (Web)
    private static final Color ROSA_FUNDO = Color.decode("#facafa");
    private static final Color ROSA_HEADER = Color.decode("#e281e3");

    // Vermelho (Mobile)
    private static final Color VERMELHO_FUNDO = Color.decode("#f69999");
    private static final Color VERMELHO_HEADER = Color.decode("#ee4c4c");

    // Cores dos badges de status
    private static final Color BADGE_CONCLUIDO = Color.decode("#befb5e");
    private static final Color BADGE_DESENVOLVIMENTO = Color.decode("#fbd05e");
    private static final Color BADGE_PLANEJADO = Color.decode("#c38cf1");

    private final CardCategoriaPanel cardSoftware;
    private final CardCategoriaPanel cardWeb;
    private final CardCategoriaPanel cardMobile;

    private final ApiClient apiClient = new ApiClient();

    public ViewCategorias() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_PAGINA);

        // Grid 1x3 (repeat(3, 1fr))
        JPanel gridCategorias = new JPanel(new GridLayout(1, 3, 16, 0));
        gridCategorias.setOpaque(false);
        gridCategorias.setBorder(new EmptyBorder(16, 16, 16, 16));

        cardSoftware = new CardCategoriaPanel(AZUL_FUNDO, AZUL_HEADER, TipoIconeCategoria.DESKTOP, "0", "Projetos de software");
        cardWeb = new CardCategoriaPanel(ROSA_FUNDO, ROSA_HEADER, TipoIconeCategoria.GLOBO, "0", "Projetos web");
        cardMobile = new CardCategoriaPanel(VERMELHO_FUNDO, VERMELHO_HEADER, TipoIconeCategoria.MOBILE, "0", "Projetos mobile");

        gridCategorias.add(cardSoftware);
        gridCategorias.add(cardWeb);
        gridCategorias.add(cardMobile);

        JScrollPane scrollPane = new JScrollPane(gridCategorias);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Busca os projetos reais na API
        carregarDados();

        // Recarrega sempre que a tela de Categorias volta a ser exibida (CardLayout),
        // assim ela reflete o que foi cadastrado, alterado ou excluído em Projetos
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                carregarDados();
            }
        });
    }

    /**
     * Busca os projetos na API (fora da thread da interface) e atualiza os três cards.
     */
    public void carregarDados() {
        new SwingWorker<List<Projeto>, Void>() {
            @Override
            protected List<Projeto> doInBackground() throws Exception {
                return apiClient.listarProjetos();
            }

            @Override
            protected void done() {
                try {
                    atualizarProjetos(get());
                } catch (Exception e) {
                    // Mantém os últimos dados exibidos; sem pop-up para não incomodar a cada troca de tela
                    e.printStackTrace();
                    apiClient.registrarLogErro("carregar_categorias", e);
                }
            }
        }.execute();
    }

    /**
     * Separa os projetos por categoria e preenche cada card.
     */
    public void atualizarProjetos(List<Projeto> projetos) {
        List<ItemProjeto> software = new ArrayList<>();
        List<ItemProjeto> web = new ArrayList<>();
        List<ItemProjeto> mobile = new ArrayList<>();

        if (projetos != null) {
            for (Projeto p : projetos) {
                ItemProjeto item = new ItemProjeto(p.getNome(), p.getStatus());

                switch (Textos.normalizar(p.getCategoria())) {
                    case "software" -> software.add(item);
                    case "web" -> web.add(item);
                    case "mobile" -> mobile.add(item);
                    default -> System.err.println("Projeto com categoria fora do padrão: " + p);
                }
            }
        }

        setProjetosSoftware(software);
        setProjetosWeb(web);
        setProjetosMobile(mobile);
    }

    // =========================================================================
    // MÉTODOS PÚBLICOS PARA POPULAR CADA CATEGORIA (EX.: DADOS DA API)
    // =========================================================================

    public void setProjetosSoftware(List<ItemProjeto> projetos) {
        cardSoftware.setProjetos(projetos);
    }

    public void setProjetosWeb(List<ItemProjeto> projetos) {
        cardWeb.setProjetos(projetos);
    }

    public void setProjetosMobile(List<ItemProjeto> projetos) {
        cardMobile.setProjetos(projetos);
    }

    public void limparTudo() {
        cardSoftware.setProjetos(List.of());
        cardWeb.setProjetos(List.of());
        cardMobile.setProjetos(List.of());
    }

    // =========================================================================
    // MODELO DE DADOS SIMPLES
    // =========================================================================

    public static class ItemProjeto {
        public final String nome;
        public final String status;

        public ItemProjeto(String nome, String status) {
            this.nome = nome;
            this.status = status;
        }
    }

    enum TipoIconeCategoria { DESKTOP, GLOBO, MOBILE }

    // =========================================================================
    // CARD DE CATEGORIA (HEADER COLORIDO + CONTADOR + TABELA)
    // =========================================================================

    static class CardCategoriaPanel extends JPanel {
        private final Color corFundo;
        private final Color corHeader;
        private final JLabel lblQuantidade;
        private final DefaultTableModel modeloTabela;

        CardCategoriaPanel(Color corFundo, Color corHeader, TipoIconeCategoria icone, String qtdInicial, String subtitulo) {
            this.corFundo = corFundo;
            this.corHeader = corHeader;

            setLayout(new BorderLayout(0, 14));
            setOpaque(false);
            setBorder(new EmptyBorder(16, 18, 18, 18));

            // Header do card (.card-categoria-header)
            JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
            header.setBackground(corHeader);
            header.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

            IconeCategoriaPanel iconePanel = new IconeCategoriaPanel(icone, COR_TEXTO_CLARO);
            iconePanel.setPreferredSize(new Dimension(24, 24));

            lblQuantidade = new JLabel(qtdInicial);
            lblQuantidade.setFont(new Font("SansSerif", Font.BOLD, 24));
            lblQuantidade.setForeground(COR_TEXTO_CLARO);

            JLabel lblSubtitulo = new JLabel(subtitulo);
            lblSubtitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblSubtitulo.setForeground(COR_TEXTO_CLARO);

            header.add(iconePanel);
            header.add(lblQuantidade);
            header.add(lblSubtitulo);

            // Tabela (.tabela-categoria)
            String[] colunas = {"Nome do Projeto", "Status"};
            modeloTabela = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable tabela = new JTable(modeloTabela);
            tabela.setRowHeight(36);
            tabela.setShowVerticalLines(false);
            tabela.setGridColor(new Color(0, 0, 0, 35));
            tabela.setBackground(Color.WHITE);
            tabela.setForeground(COR_TEXTO_ESCURO);
            tabela.setSelectionBackground(new Color(191, 155, 13, 52));
            tabela.setSelectionForeground(COR_TEXTO_ESCURO);
            tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));

            JTableHeader tableHeader = tabela.getTableHeader();
            tableHeader.setFont(new Font("SansSerif", Font.BOLD, 12));
            tableHeader.setBackground(new Color(241, 245, 249));
            tableHeader.setForeground(COR_TEXTO_ESCURO);
            tableHeader.setReorderingAllowed(false);

            tabela.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
                    lbl.setBorder(new EmptyBorder(0, 12, 0, 12));
                    return lbl;
                }
            });

            tabela.getColumnModel().getColumn(1).setCellRenderer(new StatusBadgeRenderer());
            tabela.getColumnModel().getColumn(1).setPreferredWidth(130);
            tabela.getColumnModel().getColumn(1).setMaxWidth(150);

            JScrollPane scrollTabela = new JScrollPane(tabela);
            scrollTabela.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 40), 1));
            scrollTabela.getViewport().setBackground(Color.WHITE);

            add(header, BorderLayout.NORTH);
            add(scrollTabela, BorderLayout.CENTER);
        }

        public void setProjetos(List<ItemProjeto> projetos) {
            modeloTabela.setRowCount(0);
            if (projetos != null) {
                lblQuantidade.setText(String.valueOf(projetos.size()));
                for (ItemProjeto p : projetos) {
                    modeloTabela.addRow(new Object[]{p.nome, p.status});
                }
            } else {
                lblQuantidade.setText("0");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Sombra sutil
            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 12, 12);

            // Fundo colorido da categoria
            g2.setColor(corFundo);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);

            // Borda
            g2.setColor(COR_BORDA);
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        StatusBadgeRenderer() {
            setOpaque(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value != null ? value.toString() : "";
            return new JPanel() {
                {
                    setOpaque(false);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (status.isEmpty()) return;

                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color corTag = BADGE_PLANEJADO;
                    if ("Concluído".equalsIgnoreCase(status) || "Concluido".equalsIgnoreCase(status)) {
                        corTag = BADGE_CONCLUIDO;
                    } else if ("Em desenvolvimento".equalsIgnoreCase(status)) {
                        corTag = BADGE_DESENVOLVIMENTO;
                    }

                    g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                    FontMetrics fm = g2.getFontMetrics();
                    int pillW = fm.stringWidth(status) + 18;
                    int pillH = 22;
                    int px = (getWidth() - pillW) / 2;
                    int py = (getHeight() - pillH) / 2;

                    g2.setColor(corTag);
                    g2.fill(new RoundRectangle2D.Double(px, py, pillW, pillH, 99, 99));

                    g2.setColor(COR_TEXTO_ESCURO);
                    g2.drawString(status, px + 9, py + ((pillH + fm.getAscent() - fm.getDescent()) / 2));
                    g2.dispose();
                }
            };
        }
    }

    static class IconeCategoriaPanel extends JPanel {
        private final TipoIconeCategoria tipo;
        private final Color cor;

        IconeCategoriaPanel(TipoIconeCategoria tipo, Color cor) {
            this.tipo = tipo;
            this.cor = cor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (tipo) {
                case DESKTOP -> {
                    g2.drawRoundRect(2, 3, 20, 13, 2, 2);
                    g2.drawLine(12, 16, 12, 20);
                    g2.drawLine(8, 20, 16, 20);
                }
                case GLOBO -> {
                    g2.drawOval(3, 3, 18, 18);
                    g2.drawOval(8, 3, 8, 18);
                    g2.drawLine(3, 12, 21, 12);
                }
                case MOBILE -> {
                    g2.drawRoundRect(6, 2, 12, 20, 3, 3);
                    g2.fillOval(11, 18, 2, 2);
                }
            }
            g2.dispose();
        }
    }
}