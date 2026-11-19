package barbosa.isabeli.views;

import javax.swing.*;

import barbosa.isabeli.components.NeoCard;

import java.awt.*;

public class ViewProjetos extends JPanel {
    public ViewProjetos() {
        setOpaque(false);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PROHETOS PROJETOS PROJETOS");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        NeoCard card = new NeoCard();
        card.setLayout(new FlowLayout());
        card.add(new JLabel("SOCORRO O JAVA VAI ME MATAR"));

        add(title, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
    }
}