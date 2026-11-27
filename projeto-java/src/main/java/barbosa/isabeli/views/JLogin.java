package barbosa.isabeli.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.components.ShadowButton;

public class JLogin extends PainelAutenticacao {

    /**
     * @param api              cliente da API de autenticação (compartilhado com o cadastro)
     * @param aoLogar          chamado com o e-mail do usuário quando o login é aceito
     * @param aoIrParaCadastro chamado quando o usuário clica em "Cadastre-se"
     */
    public JLogin(ApiClient api, Consumer<String> aoLogar, Runnable aoIrParaCadastro) {
        super(new Cartao(api, aoLogar, aoIrParaCadastro));
    }

    private static class Cartao extends CartaoAutenticacao {

        private final ApiClient api;
        private final Consumer<String> aoLogar;

        private final JLabel titulo = new JLabel("Login", SwingConstants.CENTER);
        private final JLabel emailLabel = new JLabel("Email:");
        private final JLabel senhaLabel = new JLabel("Senha:");
        private final JTextField campoEmail = new JTextField();
        private final JPasswordField campoSenha = new JPasswordField();
        private final ShadowButton botaoEntrar =
            new ShadowButton("Entrar", new Color(160, 255, 80));
        private final ShadowButton botaoCadastro =
            new ShadowButton("Não possui conta? Cadastre-se", new Color(130, 190, 240));

        Cartao(ApiClient api, Consumer<String> aoLogar, Runnable aoIrParaCadastro) {
            this.api = api;
            this.aoLogar = aoLogar;

            botaoCadastro.setForeground(Color.BLACK);

            add(titulo);
            add(emailLabel);
            add(campoEmail);
            add(senhaLabel);
            add(campoSenha);
            add(botaoEntrar);
            add(botaoCadastro);

            botaoEntrar.addActionListener(e -> entrar());
            campoSenha.addActionListener(e -> entrar()); // Enter no campo de senha
            botaoCadastro.addActionListener(e -> aoIrParaCadastro.run());
        }

        private void entrar() {
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword());

            if (email.isEmpty() || senha.isEmpty()) {
                aviso("Preencha o e-mail e a senha.");
                return;
            }

            botaoEntrar.setEnabled(false);

            // A chamada HTTP roda fora da thread da interface para não travar a janela
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return api.login(email, senha);
                }

                @Override
                protected void done() {
                    botaoEntrar.setEnabled(true);

                    boolean valido;
                    try {
                        valido = get();
                    } catch (Exception ex) {
                        aviso("Erro ao conectar com a API.");
                        ex.printStackTrace();
                        return;
                    }

                    if (valido) {
                        campoSenha.setText("");
                        aoLogar.accept(email);
                    } else {
                        aviso("E-mail ou senha incorretos.");
                    }
                }
            }.execute();
        }

        private void aviso(String mensagem) {
            JOptionPane.showMessageDialog(this, mensagem);
        }

        @Override
        public void doLayout() {
            posicionar(titulo, 180, 45, 246, 65);
            titulo.setFont(fonte(40));

            posicionar(emailLabel, 58, 142, 200, 25);
            emailLabel.setFont(fonte(20));
            posicionar(campoEmail, 58, 165, 492, 33);

            posicionar(senhaLabel, 58, 235, 200, 25);
            senhaLabel.setFont(fonte(20));
            posicionar(campoSenha, 58, 256, 492, 33);

            posicionar(botaoEntrar, 420, 312, 142, 40);
            botaoEntrar.setEscala(escala);
            botaoEntrar.setFont(fonte(18));

            posicionar(botaoCadastro, 160, 410, 286, 50);
            botaoCadastro.setEscala(escala);
            botaoCadastro.setFont(fonte(16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Linha abaixo do título
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke((float) (3 * escala)));
            g2.drawLine(px(242), px(101), px(364), px(101));
            g2.dispose();
        }
    }
}