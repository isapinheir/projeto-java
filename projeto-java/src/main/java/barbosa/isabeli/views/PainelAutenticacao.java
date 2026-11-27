package barbosa.isabeli.views;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Painel de fundo que centraliza o cartão e o redimensiona com a janela.
 * JLogin e JSign tinham esse mesmo código duplicado.
 */
public abstract class PainelAutenticacao extends JPanel {

    private static final int MARGEM = 40;
    private static final double ESCALA_MINIMA = 0.40;

    private final CartaoAutenticacao cartao;

    protected PainelAutenticacao(CartaoAutenticacao cartao) {
        this.cartao = cartao;

        setBackground(new Color(245, 230, 207));
        setLayout(null);
        add(cartao);
    }

    @Override
    public void doLayout() {
        double escalaX = (double) (getWidth() - MARGEM * 2) / CartaoAutenticacao.LARGURA;
        double escalaY = (double) (getHeight() - MARGEM * 2) / CartaoAutenticacao.ALTURA;
        double escala = Math.max(ESCALA_MINIMA, Math.min(Math.min(escalaX, escalaY), 1.0));

        int largura = (int) (CartaoAutenticacao.LARGURA * escala);
        int altura = (int) (CartaoAutenticacao.ALTURA * escala);

        // O cartão inclui a área da sombra (20 x 22 no tamanho original)
        cartao.setBounds(
            (getWidth() - largura) / 2,
            (getHeight() - altura) / 2,
            largura + (int) (20 * escala),
            altura + (int) (22 * escala)
        );
        cartao.setEscala(escala);
    }
}