package barbosa.isabeli.model;

public record LogReq(
        String origem,   // "SWING" ou "WEB"
        String acao,     // ex: "INICIO_SISTEMA", "CRIAR", "ALTERAR", "EXCLUIR", "ERRO"
        String detalhes, // contexto do evento
        String erro      // opcional (mensagem/stack trace quando houver)
) {}