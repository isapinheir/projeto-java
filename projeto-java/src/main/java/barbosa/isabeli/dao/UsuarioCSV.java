package barbosa.isabeli.dao;

import barbosa.isabeli.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public class UsuarioCSV {

    private static final Object LOCK = new Object();
    private final Path caminho;

    public UsuarioCSV() {
        this(Path.of("usuarios.csv"));
    }

    public UsuarioCSV(Path caminho) {
        this.caminho = caminho;
    }

    public void salvar(List<Usuario> usuarios) throws Exception {
        synchronized (LOCK) {
            if (caminho.getParent() != null) {
                Files.createDirectories(caminho.getParent());
            }

            List<String> linhas = new ArrayList<>();
            linhas.add("user;senha");

            for (Usuario usuario : usuarios) {
                String userLimpo = escaparCampo(usuario.getUser());
                String senhaLimpa = escaparCampo(usuario.getSenha());
                linhas.add(userLimpo + ";" + senhaLimpa);
            }

            Files.write(
                    caminho,
                    linhas,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        }
    }

    public List<Usuario> listar() throws Exception {
        synchronized (LOCK) {
            List<Usuario> usuarios = new ArrayList<>();

            if (!Files.exists(caminho)) {
                System.out.println("O arquivo de usuários não existe: " + caminho);
                return usuarios;
            }

            List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);

            for (int i = 0; i < linhas.size(); i++) {
                String linha = linhas.get(i);
                if (linha == null) continue;

                if (i == 0 && linha.startsWith("\uFEFF")) {
                    linha = linha.substring(1);
                }

                linha = linha.trim();
                if (linha.isEmpty()) continue;

                if (i == 0 && linha.equalsIgnoreCase("user;senha")) {
                    continue;
                }

                String[] dados = linha.split(";", -1);
                if (dados.length >= 2) {
                    String user = dados[0].trim();
                    String senha = dados[1].trim();

                    if (!user.isEmpty()) {
                        usuarios.add(new Usuario(user, senha));
                    } else {
                        System.err.println("AVISO na linha " + (i + 1) + ": usuário vazio ignorado.");
                    }
                } else {
                    System.err.println("AVISO na linha " + (i + 1) + ": formato inválido (esperado 'user;senha').");
                }
            }

            return usuarios;
        }
    }

    public boolean autenticar(String user, String senhaDigitada) throws Exception {
        if (user == null || senhaDigitada == null) {
            return false;
        }

        String userBuscado = user.trim();
        String hashDigitado = gerarHashSha256(senhaDigitada);

        for (Usuario u : listar()) {
            if (u.getUser().equalsIgnoreCase(userBuscado)) {
                return u.getSenha().equals(hashDigitado) || u.getSenha().equals(senhaDigitada);
            }
        }
        return false;
    }

    public void cadastrarUsuario(String user, String senhaPlana) throws Exception {
        if (user == null || user.isBlank() || senhaPlana == null || senhaPlana.isBlank()) {
            throw new IllegalArgumentException("Usuário e senha são obrigatórios.");
        }

        String userFormatado = user.trim();
        List<Usuario> usuarios = listar();

        boolean jaExiste = usuarios.stream()
                .anyMatch(u -> u.getUser().equalsIgnoreCase(userFormatado));

        if (jaExiste) {
            throw new IllegalStateException("Já existe um usuário com esse nome.");
        }

        usuarios.add(new Usuario(userFormatado, gerarHashSha256(senhaPlana)));
        salvar(usuarios);
    }

    private String escaparCampo(String valor) {
        if (valor == null) return "";
        return valor.replace(";", "").replace("\r", "").replace("\n", "").trim();
    }

    public static String gerarHashSha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-256 indisponível", e);
        }
    }
}