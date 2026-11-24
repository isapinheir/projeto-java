package barbosa.isabeli.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Cores {
    
	public static final Map<String, Color> PALETA = new HashMap<>();
	
	static {
	    // --- Sidebar ---
		PALETA.put("cabecalho", Color.decode("#edd5af"));
		PALETA.put("fundo", Color.decode("#F9F5EC"));
	    PALETA.put("sidebarFundo", Color.decode("#F4E6CF"));
	    PALETA.put("sidebarHover", Color.decode("#CBABFD"));
	    PALETA.put("sidebarAtivo", Color.decode("#9b5efb"));
	
	    // --- Status e Ações ---
	    PALETA.put("statusPrimario", Color.decode("#4c65ef"));
	    PALETA.put("statusPrimarioHover", Color.decode("#1289c8"));
	    PALETA.put("statusPrimarioAtivo", Color.decode("#0f72a7"));
	
	    PALETA.put("statusAlerta", Color.decode("#c8af12"));
	    PALETA.put("statusAlertaHover", Color.decode("#12c8ac"));
	    // Nota: não tinha cor p/ status-alerta-ativo.
	
	    PALETA.put("statusErro", Color.decode("#c81212"));
	    PALETA.put("statusErroHover", Color.decode("#c8126d"));
	    PALETA.put("statusErroAtivo", Color.decode("#a50f5a"));
	
	    PALETA.put("statusSucesso", Color.decode("#12c854"));
	    PALETA.put("statusSucessoHover", Color.decode("#12c8ac"));
	    PALETA.put("statusSucessoAtivo", Color.decode("#0e9782"));
	
	    // --- Cores Gerais ---
	    PALETA.put("azul", Color.decode("#89c0f4"));
	    PALETA.put("azulEscuro", Color.decode("#0d52bf"));
	    PALETA.put("verde", Color.decode("#befb5e"));
	    PALETA.put("verdeEscuro", Color.decode("#28a745"));
	    PALETA.put("amarelo", Color.decode("#fbd05e"));
	    PALETA.put("amareloEscuro", Color.decode("#ff9800"));
	    PALETA.put("rosa", Color.decode("#facafa"));
	    PALETA.put("rosaEscuro", Color.decode("#e281e3"));
	    PALETA.put("roxo", Color.decode("#c38cf1"));
	    PALETA.put("roxoEscuro", Color.decode("#9c27b0"));
	    PALETA.put("vermelho", Color.decode("#f69999"));
	    PALETA.put("vermelhoEscuro", Color.decode("#ee4c4c"));
	    
	    // --- Tabela ---
	    PALETA.put("hoverTabela", new Color(191, 155, 13, 52));
	    
	    // --- Texto ---
	    PALETA.put("textoPrimario", new Color(30, 41, 59));
	    PALETA.put("textoSecundario", new Color(71, 85, 105));
	}
	
	// retorna preto caso o nome da cor não exista
    public static Color get(String name) {
        return PALETA.getOrDefault(name, Color.BLACK);
    }
}