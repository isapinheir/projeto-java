package barbosa.isabeli.views;

import java.awt.Color;
import java.awt.Component;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import barbosa.isabeli.client.ApiClient;
import barbosa.isabeli.components.ShadowButton;

public class JSign extends PainelAutenticacao {

    /**
     * @param api           cliente da API de autenticação (compartilhado com o login)
     * @param aoIrParaLogin chamado após um cadastro bem-sucedido e ao clicar em "Entrar"
     */
    public JSign(ApiClient api, Runnable aoIrParaLogin) {
        super(new Cartao(api, aoIrParaLogin));
    }

    private static class Cartao extends CartaoAutenticacao {

        private static final Pattern REGEX_NOME =
            Pattern.compile("^[\\p{L}]+(?:[ '\\-][\\p{L}]+)*$");
        private static final Pattern REGEX_EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        private static final Pattern REGEX_SENHA =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

        private final ApiClient api;
        private final Runnable aoIrParaLogin;

        private final JLabel titulo =
            new JLabel("<html><u>Cadastro</u></html>", SwingConstants.CENTER);
        private final JLabel nomeLabel = new JLabel("Nome:");
        private final JLabel emailLabel = new JLabel("Email:");
        private final JLabel senhaLabel = new JLabel("Senha:");
        private final JLabel confirmarSenhaLabel = new JLabel("Confirme a Senha:");

        private final JTextField campoNome = new JTextField();
        private final JTextField campoEmail = new JTextField();
        private final JPasswordField campoSenha = new JPasswordField();
        private final JPasswordField campoConfirmarSenha = new JPasswordField();

        private final ShadowButton botaoCadastrar =
            new ShadowButton("Cadastrar", new Color(190, 130, 240));
        private final ShadowButton botaoEntrar =
            new ShadowButton("Já possui conta? Entrar", new Color(255, 205, 65));

        Cartao(ApiClient api, Runnable aoIrParaLogin) {
            this.api = api;
            this.aoIrParaLogin = aoIrParaLogin;

            for (Component c : new Component[] {
                titulo, nomeLabel, campoNome, emailLabel, campoEmail,
                senhaLabel, campoSenha, confirmarSenhaLabel, campoConfirmarSenha,
                botaoCadastrar, botaoEntrar
            }) {
                add(c);
            }

            botaoCadastrar.addActionListener(e -> cadastrar());
            campoConfirmarSenha.addActionListener(e -> cadastrar()); // Enter no último campo
            botaoEntrar.addActionListener(e -> aoIrParaLogin.run());
        }

        private void cadastrar() {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword());
            String confirmar = new String(campoConfirmarSenha.getPassword());

            if (!camposValidos(nome, email, senha, confirmar)) {
                return;
            }

            botaoCadastrar.setEnabled(false);

            // A chamada HTTP roda fora da thread da interface para não travar a janela
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return api.cadastrar(email, senha);
                }

                @Override
                protected void done() {
                    botaoCadastrar.setEnabled(true);

                    boolean cadastrado;
                    try {
                        cadastrado = get();
                    } catch (Exception ex) {
                        aviso("Erro ao conectar com a API.");
                        ex.printStackTrace();
                        return;
                    }

                    if (cadastrado) {
                        limparCampos();
                        aviso("Cadastro realizado com sucesso!");
                        aoIrParaLogin.run();
                    } else {
                        aviso("Não foi possível realizar o cadastro.");
                    }
                }
            }.execute();
        }

        private boolean camposValidos(String nome, String email, String senha, String confirmar) {
            if (nome.isEmpty()) {
                return invalido("Preencha o nome.", campoNome);
            }
            if (!REGEX_NOME.matcher(nome).matches()) {
                return invalido("O nome deve conter apenas letras e espaços.", campoNome);
            }
            if (!REGEX_EMAIL.matcher(email).matches()) {
                return invalido("Digite um e-mail válido.", campoEmail);
            }
            if (!REGEX_SENHA.matcher(senha).matches()) {
                return invalido(
                    "A senha deve possuir:\n"
                    + "- 8 caracteres ou mais\n"
                    + "- Uma letra maiúscula\n"
                    + "- Uma letra minúscula\n"
                    + "- Um número\n"
                    + "- Um símbolo",
                    campoSenha
                );
            }
            if (!senha.equals(confirmar)) {
                return invalido("As senhas não coincidem.", campoConfirmarSenha);
            }
            return true;
        }

        /** Mostra a mensagem, foca o campo com problema e devolve false. */
        private boolean invalido(String mensagem, JComponent campo) {
            aviso(mensagem);
            campo.requestFocus();
            return false;
        }

        private void aviso(String mensagem) {
            JOptionPane.showMessageDialog(this, mensagem);
        }

        private void limparCampos() {
            campoNome.setText("");
            campoEmail.setText("");
            campoSenha.setText("");
            campoConfirmarSenha.setText("");
        }

        @Override
        public void doLayout() {
            posicionar(titulo, 180, 35, 246, 65);
            titulo.setFont(fonte(40));

            posicionar(nomeLabel, 58, 130, 200, 25);
            nomeLabel.setFont(fonte(16));
            posicionar(campoNome, 58, 149, 492, 33);

            posicionar(emailLabel, 58, 205, 200, 25);
            emailLabel.setFont(fonte(20));
            posicionar(campoEmail, 58, 230, 492, 33);

            posicionar(senhaLabel, 60, 288, 200, 25);
            senhaLabel.setFont(fonte(20));
            posicionar(campoSenha, 62, 307, 211, 33);

            posicionar(confirmarSenhaLabel, 333, 288, 250, 25);
            confirmarSenhaLabel.setFont(fonte(20));
            posicionar(campoConfirmarSenha, 334, 307, 214, 33);

            posicionar(botaoCadastrar, 410, 345, 142, 40);
            botaoCadastrar.setEscala(escala);
            botaoCadastrar.setFont(fonte(16));

            posicionar(botaoEntrar, 205, 415, 220, 50);
            botaoEntrar.setEscala(escala);
            botaoEntrar.setFont(fonte(16));
        }
    }
}