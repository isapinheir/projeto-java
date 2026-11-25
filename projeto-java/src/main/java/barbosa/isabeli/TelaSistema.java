package barbosa.isabeli;

import com.kitfox.svg.app.beans.SVGIcon;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.components.NeoButton;
import barbosa.isabeli.utils.Cores;
import barbosa.isabeli.utils.Icones;
import barbosa.isabeli.views.*;

public class TelaSistema extends JFrame{
    
    private CardLayout cardLayout;
    private JPanel conteudo;
    private JButton botaoAtivoAtual;
    private ApiClient apiClient;
    private final Timer timer;
    
    private String horario;
    private String dia;
    private String data;
    private JButton usuario;

    
    /*
     * as dimensões estão péssimas!!!
     * como trocar a cor dos menus da sidebar?
     * não tá dando pra ler o arquivo das fontes
     * mexer no foco dos elementos depois
     * 
     * */

    public TelaSistema() {
        setTitle("Sistema de Projetos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Cores.get("fundo"));
        setLayout(new BorderLayout());
        
        String usuarioLogado = "João Silva";
        apiClient = new ApiClient();
		apiClient.registrarLogSessao("login", usuarioLogado);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	apiClient.registrarLogSessao("logout", usuarioLogado);
            }
        });
		
	    atualizarRelogio();
	    timer = new Timer(10_000, e -> atualizarRelogio());
	    timer.start();
        
        // ========= ÍCONES =========
        
        ImageIcon iconeDashboard = Icones.get("home.svg", 18, 18, Color.BLACK);
        ImageIcon iconeProjetos = Icones.get("newspaper.svg", 18, 18, Color.BLACK);
        ImageIcon iconeCategorias = Icones.get("menu.svg", 18, 18, Color.BLACK);
        ImageIcon iconePessoa = Icones.get("person.svg", 18, 18, Color.WHITE);

        // ========= SIDEBAR =========
   
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Cores.get("sidebarFundo"));
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
        
        JButton menuDashboard = criarBtn("Dashboard", iconeDashboard);
        JButton menuProjetos = criarBtn("Projetos", iconeProjetos);
        JButton menuCategorias = criarBtn("Categorias", iconeCategorias);

        sidebar.add(menuDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(menuProjetos);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(menuCategorias);
        
        setMenuAtivo(menuDashboard); 

        // ========= CABEÇALHO =========
        
        JPanel cabecalho = new JPanel();
        cabecalho.setBackground(Cores.get("cabecalho"));
        cabecalho.setPreferredSize(new Dimension(getWidth(), 100));
      
        JPanel relogio = new JPanel();
        JLabel diaSemana = new JLabel(dia);
        JLabel dataHora = new JLabel(data + ", " + horario);
        relogio.add(diaSemana);
        relogio.add(dataHora);
        
        usuario = new JButton("João Silva");
        usuario.setHorizontalAlignment(SwingConstants.LEFT);
        usuario.setIconTextGap(10);
        usuario.setIcon(iconePessoa);
      
        cabecalho.add(usuario);
        cabecalho.add(relogio);

        // ========= CONTEÚDO =========
        
        cardLayout = new CardLayout();
        conteudo = new JPanel(cardLayout);
        conteudo.setOpaque(false);

        ViewDashboard dashboard = new ViewDashboard();
        ViewProjetos projetos = new ViewProjetos();
        conteudo.add(dashboard, "DASHBOARD");
        conteudo.add(projetos, "PROJETOS");

        menuDashboard.addActionListener(e -> cardLayout.show(conteudo, "DASHBOARD"));
        menuProjetos.addActionListener(e -> cardLayout.show(conteudo, "PROJETOS"));

        add(sidebar, BorderLayout.WEST);
        add(cabecalho, BorderLayout.NORTH);
        add(conteudo, BorderLayout.CENTER);
    }
    
    // ========= MÉTODOS AUXIIARES =========
    
    private JButton criarBtn(String text, ImageIcon icone) {
        JButton item = new JButton(text);
        item.setMaximumSize(new Dimension(190, 40));
        item.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setIconTextGap(10);
        item.setFocusPainted(false);
        item.setBorderPainted(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setContentAreaFilled(false);
        item.setForeground(Color.BLACK);
        
        item.setIcon(icone);
        
        item.addActionListener(e -> setMenuAtivo(item));

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

        String diaMin = agora.format(DateTimeFormatter.ofPattern("EEEE", Locale.of("pt", "BR")));
        
        dia = diaMin.substring(0, 1).toUpperCase(Locale.of("pt", "BR")) + diaMin.substring(1);
        data = agora.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.of("pt", "BR")));
        horario = agora.format(DateTimeFormatter.ofPattern("HH:mm", Locale.of("pt", "BR")));

    } 
}
