package barbosa.isabeli;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import barbosa.isabeli.components.NeoButton;
import barbosa.isabeli.utils.Cores;

import barbosa.isabeli.views.*;

public class TelaSistema extends JFrame{
    
    private CardLayout cardLayout;
    private JPanel conteudo;
    
    /*
     * as dimensões estão péssimas!!!
     * como trocar a cor dos menus da sidebar?
     * não tá dando pra ler o arquivo das fontes
     * 
     * */

    public TelaSistema() {
        setTitle("Sistema de Projetos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Cores.get("fundo"));
        setLayout(new BorderLayout());

        // sidebar        
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
        
        URL iconeDashboard = getClass().getResource("/assets/img/home.svg");
        URL iconeProjetos = getClass().getResource("/assets/img/newspaper.svg");
        URL iconeCategorias = getClass().getResource("/assets/img/menu.svg");

        sidebar.add(createSidebarItem("Dashboard", iconeDashboard, false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createSidebarItem("Projetos", iconeProjetos, true)); // Ativo (roxo)
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createSidebarItem("Categorias", iconeCategorias, false));

        // botões
        JButton btnHome = new NeoButton("Home", Cores.get("azul"));
        JButton btnSettings = new NeoButton("Configurações", Cores.get("verde"));

        sidebar.add(btnHome);
        sidebar.add(btnSettings);

        // conteudo
        cardLayout = new CardLayout();
        conteudo = new JPanel(cardLayout);
        conteudo.setOpaque(false);

        ViewDashboard dashboard = new ViewDashboard();
        ViewProjetos projetos = new ViewProjetos();
        conteudo.add(dashboard, "DASHBOARD");
        conteudo.add(projetos, "PROJETOS");

        btnHome.addActionListener(e -> cardLayout.show(conteudo, "DASHBOARD"));
        btnSettings.addActionListener(e -> cardLayout.show(conteudo, "PROJETOS"));

        add(sidebar, BorderLayout.WEST);
        add(conteudo, BorderLayout.CENTER);
    }
    
    private JComponent createSidebarItem(String text, URL icone, boolean active) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        item.setMaximumSize(new Dimension(190, 40)); // AAA
        item.setOpaque(active);
        if (active) {
            item.setBackground(Cores.get("sidebarAtivo"));
        } else {
            item.setOpaque(false);
        }
        
        if (icone != null) {
            ImageIcon imgIcon = new ImageIcon(icone);
            // Image imagemRedimensionada = imgIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            // imgIcon = new ImageIcon(im-agemRedimensionada);
            
            JLabel lblIcon = new JLabel(imgIcon);
            item.add(lblIcon);
        }
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(active ? Color.WHITE : Color.BLACK);
      
        item.add(lbl);
        
        
        return item;
    }
}
