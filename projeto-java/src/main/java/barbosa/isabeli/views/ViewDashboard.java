package barbosa.isabeli.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;

public class ViewDashboard extends JPanel { 

    // Paleta de cores extraída do seu CSS
    private static final Color COR_FUNDO_TELA = new Color(248, 250, 252);
    private static final Color COR_FUNDO_CARD = Color.WHITE;
    private static final Color COR_BORDA_CARD = new Color(226, 232, 240);
    private static final Color COR_TEXTO_PRINCIPAL = new Color(30, 41, 59);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(100, 116, 139);
    private static final Color COR_TEXTO_MUTADO = new Color(148, 163, 184);

    // Cores dos gráficos
    private static final Color COR_AZUL_SW = Color.decode("#89c0f4");
    private static final Color COR_AMARELO_WEB = Color.decode("#fbd05e");
    private static final Color COR_ROXO_MOB = Color.decode("#c38cf1");
    private static final Color COR_VERDE_CONCLUIDO = Color.decode("#befb5e");
    private static final Color COR_VERMELHO_PLANEJADO = Color.decode("#c81212");
    private static final Color COR_FUNDO_ROSQUINHA = Color.decode("#eef4f0");

    // Labels dinâmicos (equivalentes aos IDs do HTML)
    private JLabel lblDashboardTotal;
    private JLabel lblDashboardNaoConcluido;
    private JLabel lblDashboardSw;
    private JLabel lblDashboardWeb;
    private JLabel lblDashboardMob;

    private JLabel lblPorcConcluidos;
    private JLabel lblPorcDesenvolvendo;
    private JLabel lblPorcPlanejados;

    // Componentes de gráficos customizados
    private GraficoColunasPanel graficoColunas;
    private GraficoRosquinhaPanel graficoRosquinha;
    
    // ==========================================
    // CONSTRUTOR
    // ==========================================

    public ViewDashboard() {
        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_TELA);

        JPanel mainContainer = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel gridTopo = new JPanel(new GridLayout(1, 2, 16, 0));
        
        
        mainContainer.setBackground(COR_FUNDO_TELA);
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;


        
        gridTopo.setOpaque(false);

        lblDashboardTotal = criarTituloCard("0");
        lblDashboardNaoConcluido = criarTituloCard("0");

        gridTopo.add(criarCardMetrica(lblDashboardTotal, "Total de projetos"));
        gridTopo.add(criarCardMetrica(lblDashboardNaoConcluido, "Projetos não concluídos"));

        gbc.gridy = 0;
        gbc.weighty = 0.15;
        mainContainer.add(gridTopo, gbc);

        
        JPanel gridMeio = new JPanel(new GridBagLayout());
        gridMeio.setOpaque(false);

        GridBagConstraints gbcMeio = new GridBagConstraints();
        gbcMeio.fill = GridBagConstraints.BOTH;
        gbcMeio.gridy = 0;
        gbcMeio.weighty = 1.0;

        // Card Esquerdo: Gráfico de Colunas (3fr -> weightx = 0.75)
        gbcMeio.gridx = 0;
        gbcMeio.weightx = 0.75;
        gbcMeio.insets = new Insets(0, 0, 0, 8);
        gridMeio.add(criarCardGraficoColunas(), gbcMeio);

        // Card Direito: Gráfico de Rosquinha (1fr -> weightx = 0.25)
        gbcMeio.gridx = 1;
        gbcMeio.weightx = 0.25;
        gbcMeio.insets = new Insets(0, 8, 0, 0);
        gridMeio.add(criarCardGraficoRosquinha(), gbcMeio);

        gbc.gridy = 1;
        gbc.weighty = 0.70;
        mainContainer.add(gridMeio, gbc);

        // ==========================================
        // 3. GRID EMBAIXO (3 Colunas iguais)
        // ==========================================
        JPanel gridEmbaixo = new JPanel(new GridLayout(1, 3, 16, 0));
        gridEmbaixo.setOpaque(false);

        lblDashboardSw = criarTituloCard("0");
        lblDashboardWeb = criarTituloCard("0");
        lblDashboardMob = criarTituloCard("0");

        gridEmbaixo.add(criarCardMetrica(lblDashboardSw, "Projetos de software"));
        gridEmbaixo.add(criarCardMetrica(lblDashboardWeb, "Projetos web"));
        gridEmbaixo.add(criarCardMetrica(lblDashboardMob, "Projetos mobile"));

        gbc.gridy = 2;
        gbc.weighty = 0.15;
        mainContainer.add(gridEmbaixo, gbc);

        // Adiciona barra de rolagem caso a janela seja reduzida
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Exemplo de carregamento inicial de dados (pode ser alterado para 0)
        atualizarDashboard(12, 5, 4, 5, 3, 7, 3, 2);
    }

    /**
     * Método para atualizar todos os valores e gráficos da tela dinamicamente.
     */
    public void atualizarDashboard(int total, int naoConcluidos,
                                   int qtdSw, int qtdWeb, int qtdMob,
                                   int concluidos, int desenvolvendo, int planejados) {
        lblDashboardTotal.setText(String.valueOf(total));
        lblDashboardNaoConcluido.setText(String.valueOf(naoConcluidos));

        lblDashboardSw.setText(String.valueOf(qtdSw));
        lblDashboardWeb.setText(String.valueOf(qtdWeb));
        lblDashboardMob.setText(String.valueOf(qtdMob));

        graficoColunas.setValores(qtdSw, qtdWeb, qtdMob);
        graficoRosquinha.setValores(concluidos, desenvolvendo, planejados);

        int somaEstagios = concluidos + desenvolvendo + planejados;
        if (somaEstagios > 0) {
            lblPorcConcluidos.setText(Math.round((concluidos * 100.0) / somaEstagios) + "%");
            lblPorcDesenvolvendo.setText(Math.round((desenvolvendo * 100.0) / somaEstagios) + "%");
            lblPorcPlanejados.setText(Math.round((planejados * 100.0) / somaEstagios) + "%");
        } else {
            lblPorcConcluidos.setText("0%");
            lblPorcDesenvolvendo.setText("0%");
            lblPorcPlanejados.setText("0%");
        }
    }

    // =========================================================================
    // MÉTODOS AUXILIARES DE CRIAÇÃO DE CARDS E COMPONENTES
    // =========================================================================

    private JPanel criarCardMetrica(JLabel lblNumero, String textoSubtitulo) {
        CardPanel card = new CardPanel();
        card.setLayout(new GridBagLayout());

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);

        JLabel lblSub = criarSubtituloCard(textoSubtitulo);
        conteudo.add(lblNumero);
        conteudo.add(Box.createVerticalStrut(4));
        conteudo.add(lblSub);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(conteudo, gbc);

        return card;
    }

    private JPanel criarCardGraficoColunas() {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout(0, 16));

        JPanel cabecalho = new JPanel();
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setOpaque(false);
        cabecalho.add(criarTituloSecao("Projetos"));
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(criarSubtituloCard("por categoria"));

        graficoColunas = new GraficoColunasPanel();

        card.add(cabecalho, BorderLayout.NORTH);
        card.add(graficoColunas, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarCardGraficoRosquinha() {
        CardPanel card = new CardPanel();
        card.setLayout(new BorderLayout(0, 12));

        JPanel cabecalho = new JPanel();
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.setOpaque(false);
        cabecalho.add(criarTituloSecao("Estágios"));
        cabecalho.add(Box.createVerticalStrut(4));
        cabecalho.add(criarSubtituloCard("de desenvolvimento"));

        graficoRosquinha = new GraficoRosquinhaPanel();

        JPanel painelLegendas = new JPanel(new GridLayout(2, 2, 12, 8));
        painelLegendas.setOpaque(false);

        lblPorcConcluidos = new JLabel("0%");
        lblPorcDesenvolvendo = new JLabel("0%");
        lblPorcPlanejados = new JLabel("0%");

        painelLegendas.add(criarItemLegenda(COR_VERDE_CONCLUIDO, "Concluídos:", lblPorcConcluidos));
        painelLegendas.add(criarItemLegenda(COR_AZUL_SW, "Desenvolvendo:", lblPorcDesenvolvendo));
        painelLegendas.add(criarItemLegenda(COR_VERMELHO_PLANEJADO, "Planejados:", lblPorcPlanejados));

        card.add(cabecalho, BorderLayout.NORTH);
        card.add(graficoRosquinha, BorderLayout.CENTER);
        card.add(painelLegendas, BorderLayout.SOUTH);
        return card;
    }

    private JPanel criarItemLegenda(Color corPonto, String texto, JLabel lblPorcentagem) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);

        JPanel ponto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corPonto);
                g2.fillOval(0, 0, 8, 8);
            }
        };
        ponto.setPreferredSize(new Dimension(8, 8));
        ponto.setOpaque(false);

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTexto.setForeground(COR_TEXTO_PRINCIPAL);

        lblPorcentagem.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPorcentagem.setForeground(COR_TEXTO_PRINCIPAL);

        item.add(ponto);
        item.add(lblTexto);
        item.add(lblPorcentagem);
        return item;
    }

    private JLabel criarTituloCard(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        lbl.setForeground(COR_TEXTO_PRINCIPAL);
        return lbl;
    }

    private JLabel criarTituloSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(COR_TEXTO_PRINCIPAL);
        return lbl;
    }

    private JLabel criarSubtituloCard(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(COR_TEXTO_SECUNDARIO);
        return lbl;
    }

    // =========================================================================
    // CLASSES CUSTOMIZADAS (CARD, GRÁFICO DE COLUNAS E GRÁFICO DE ROSCA)
    // =========================================================================

    static class CardPanel extends JPanel {
        public CardPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(18, 22, 18, 22));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 14;
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, arc, arc);

            g2.setColor(COR_FUNDO_CARD);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, arc, arc);

            g2.setColor(COR_BORDA_CARD);
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, arc, arc);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class GraficoColunasPanel extends JPanel {
        private int sw = 0, web = 0, mob = 0;

        public GraficoColunasPanel() {
            setOpaque(false);
        }

        public void setValores(int sw, int web, int mob) {
            this.sw = sw;
            this.web = web;
            this.mob = mob;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int margemInferior = 30;
            int margemSuperior = 30;
            int areaUtilAltura = height - margemInferior - margemSuperior;

            int maxValor = Math.max(1, Math.max(sw, Math.max(web, mob)));
            int larguraBarra = Math.min(96, width / 5);

            String[] labels = {"SOFTWARE", "WEB", "MOBILE"};
            int[] valores = {sw, web, mob};
            Color[] cores = {COR_AZUL_SW, COR_AMARELO_WEB, COR_ROXO_MOB};

            int espacamento = width / 4;

            for (int i = 0; i < 3; i++) {
                int centroX = espacamento * (i + 1);
                int xBarra = centroX - (larguraBarra / 2);

                int alturaBarra = (valores[i] == 0)
                        ? 8
                        : Math.max(8, (int) ((valores[i] / (double) maxValor) * areaUtilAltura));

                int yBarra = height - margemInferior - alturaBarra;

                g2.setColor(cores[i]);
                g2.fillRoundRect(xBarra, yBarra, larguraBarra, alturaBarra, 8, 8);

                g2.setColor(COR_TEXTO_MUTADO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                String strValor = String.valueOf(valores[i]);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(strValor, centroX - (fm.stringWidth(strValor) / 2), yBarra - 8);

                String strLabel = labels[i];
                g2.drawString(strLabel, centroX - (fm.stringWidth(strLabel) / 2), height - 10);
            }

            g2.dispose();
        }
    }

    static class GraficoRosquinhaPanel extends JPanel {
        private int concluidos = 0, desenvolvendo = 0, planejados = 0;

        public GraficoRosquinhaPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(150, 150));
        }

        public void setValores(int concluidos, int desenvolvendo, int planejados) {
            this.concluidos = concluidos;
            this.desenvolvendo = desenvolvendo;
            this.planejados = planejados;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int tamanho = Math.min(getWidth(), getHeight()) - 20;
            tamanho = Math.max(100, Math.min(tamanho, 150));

            int x = (getWidth() - tamanho) / 2;
            int y = (getHeight() - tamanho) / 2;
            float espessura = tamanho * 0.16f;

            g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));

            g2.setColor(COR_FUNDO_ROSQUINHA);
            g2.drawOval(x, y, tamanho, tamanho);

            int total = concluidos + desenvolvendo + planejados;
            if (total > 0) {
                double anguloInicial = 90.0;

                double anguloConcluidos = (concluidos / (double) total) * 360.0;
                double anguloDesenvolvendo = (desenvolvendo / (double) total) * 360.0;
                double anguloPlanejados = (planejados / (double) total) * 360.0;

                if (anguloConcluidos > 0) {
                    g2.setColor(COR_VERDE_CONCLUIDO);
                    g2.draw(new Arc2D.Double(x, y, tamanho, tamanho, anguloInicial, -anguloConcluidos, Arc2D.OPEN));
                    anguloInicial -= anguloConcluidos;
                }

                if (anguloDesenvolvendo > 0) {
                    g2.setColor(COR_AZUL_SW);
                    g2.draw(new Arc2D.Double(x, y, tamanho, tamanho, anguloInicial, -anguloDesenvolvendo, Arc2D.OPEN));
                    anguloInicial -= anguloDesenvolvendo;
                }

                if (anguloPlanejados > 0) {
                    g2.setColor(COR_VERMELHO_PLANEJADO);
                    g2.draw(new Arc2D.Double(x, y, tamanho, tamanho, anguloInicial, -anguloPlanejados, Arc2D.OPEN));
                }
            }

            g2.dispose();
        }
    }
}