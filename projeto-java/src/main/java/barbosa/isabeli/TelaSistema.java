package barbosa.isabeli;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.utils.Cores;
import barbosa.isabeli.utils.Icones;
import barbosa.isabeli.views.*;

/**
 * Janela central. Começa na tela de login (ou cadastro) e, depois que a API
 * aceita o login, mostra o sistema (sidebar + cabeçalho + conteúdo).
 */
public class TelaSistema extends JFrame {

    private static final String TELA_LOGIN = "LOGIN";
    private static final String TELA_CADASTRO = "CADASTRO";
    private static final String TELA_SISTEMA = "SISTEMA";

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final CardLayout telas = new CardLayout();
    private final JPanel raiz = new JPanel(telas);

    private final ApiClient apiClient = new ApiClient();

    private final JLabel diaSemana = new JLabel();
    private final JLabel dataHora = new JLabel();
    private final JButton usuario = new JButton();

    private JButton botaoAtivoAtual;
    private String usuarioLogado;
    private boolean sistemaCriado;

    /*
     * as dimensões estão péssimas!!!
     * como trocar a cor dos menus da sidebar?
     * não tá dando pra ler o arquivo das fontes
     * mexer no foco dos elementos depois
     */

    public TelaSistema() {
        setTitle("Sistema de Projetos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Cores.get("fundo"));

        // ========= LOGIN / CADASTRO =========

        raiz.setOpaque(false);
        raiz.add(
            new JLogin(apiClient, this::entrarNoSistema, () -> telas.show(raiz, TELA_CADASTRO)),
            TELA_LOGIN
        );
        raiz.add(
            new JSign(apiClient, () -> telas.show(raiz, TELA_LOGIN)),
            TELA_CADASTRO
        );
        add(raiz, BorderLayout.CENTER);
        telas.show(raiz, TELA_LOGIN);

        // ========= SESSÃO / RELÓGIO =========

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (usuarioLogado != null) {
                    apiClient.registrarLogSessao("logout", usuarioLogado);
                }
            }
        });

        atualizarRelogio();
        new Timer(10_000, e -> atualizarRelogio()).start();
    }

    // ========= LOGIN =========

    private void entrarNoSistema(String email) {
        usuarioLogado = email;
        usuario.setText(email);

        // As views só são criadas depois do login, para não consultar a API antes de autenticar
        if (!sistemaCriado) {
            raiz.add(montarSistema(), TELA_SISTEMA);
            sistemaCriado = true;
        }

        apiClient.registrarLogSessao("login", email);
        telas.show(raiz, TELA_SISTEMA);
    }

    // ========= SISTEMA =========

    private JPanel montarSistema() {
        ImageIcon iconeDashboard = Icones.get("home.svg", 18, 18, Color.BLACK);
        ImageIcon iconeProjetos = Icones.get("newspaper.svg", 18, 18, Color.BLACK);
        ImageIcon iconeCategorias = Icones.get("menu.svg", 18, 18, Color.BLACK);
        ImageIcon iconePessoa = Icones.get("person.svg", 18, 18, Color.WHITE);

        // Conteúdo
        CardLayout cardLayout = new CardLayout();
        JPanel conteudo = new JPanel(cardLayout);
        conteudo.setOpaque(false);
        conteudo.add(new ViewDashboard(), "DASHBOARD");
        conteudo.add(new ViewProjetos(), "PROJETOS");
        conteudo.add(new ViewCategorias(), "CATEGORIAS");

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Cores.get("sidebarFundo"));
        sidebar.setPreferredSize(new Dimension(270, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel logoLabel = new JLabel("<html><b>Sistema</b><br>de Projetos</html>");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logoLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton menuDashboard = criarBtn("Dashboard", iconeDashboard,
            () -> cardLayout.show(conteudo, "DASHBOARD"));
        JButton menuProjetos = criarBtn("Projetos", iconeProjetos,
            () -> cardLayout.show(conteudo, "PROJETOS"));
        JButton menuCategorias = criarBtn("Categorias", iconeCategorias,
            () -> cardLayout.show(conteudo, "CATEGORIAS"));

        sidebar.add(menuDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(menuProjetos);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(menuCategorias);

        setMenuAtivo(menuDashboard);

        // Cabeçalho
        JPanel relogio = new JPanel();
        relogio.add(diaSemana);
        relogio.add(dataHora);

        usuario.setHorizontalAlignment(SwingConstants.LEFT);
        usuario.setIconTextGap(10);
        usuario.setIcon(iconePessoa);

        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.get("cabecalho"));
        cabecalho.setPreferredSize(new Dimension(0, 100));
        cabecalho.add(usuario);
        cabecalho.add(relogio);

        JPanel sistema = new JPanel(new BorderLayout());
        sistema.setOpaque(false);
        sistema.add(sidebar, BorderLayout.WEST);
        sistema.add(cabecalho, BorderLayout.NORTH);
        sistema.add(conteudo, BorderLayout.CENTER);
        return sistema;
    }

    // ========= MÉTODOS AUXILIARES =========

    private JButton criarBtn(String text, ImageIcon icone, Runnable acao) {
        JButton item = new JButton(text);
        item.setMaximumSize(new Dimension(230, 40));
        item.setFont(new Font("SansSerif", Font.BOLD, 13));

        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setIconTextGap(10);
        item.setFocusPainted(false);
        item.setBorderPainted(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setContentAreaFilled(false);
        item.setForeground(Color.BLACK);

        item.setIcon(icone);

        item.addActionListener(e -> {
            setMenuAtivo(item);
            acao.run();
        });

        return item;
    }

    private void setMenuAtivo(JButton novoBotao) {
        if (botaoAtivoAtual != null) {
            botaoAtivoAtual.setContentAreaFilled(false);
            botaoAtivoAtual.setForeground(Color.BLACK);
            Icones.recolorir(botaoAtivoAtual.getIcon(), Color.BLACK);
        }

        novoBotao.setContentAreaFilled(true);
        novoBotao.setBackground(Cores.get("sidebarAtivo"));
        novoBotao.setForeground(Color.WHITE);
        Icones.recolorir(novoBotao.getIcon(), Color.WHITE);

        botaoAtivoAtual = novoBotao;
    }

    private void atualizarRelogio() {
        LocalDateTime agora = LocalDateTime.now();

        String diaMin = agora.format(DateTimeFormatter.ofPattern("EEEE", PT_BR));
        String dia = diaMin.substring(0, 1).toUpperCase(PT_BR) + diaMin.substring(1);
        String data = agora.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", PT_BR));
        String horario = agora.format(DateTimeFormatter.ofPattern("HH:mm", PT_BR));

        // Antes o timer só alterava variáveis; os JLabels nunca eram atualizados
        diaSemana.setText(dia);
        dataHora.setText(data + ", " + horario);
    }
}