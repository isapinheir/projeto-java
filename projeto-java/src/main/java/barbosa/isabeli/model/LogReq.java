package barbosa.isabeli.model;

public record LogReq(
        String origem,   
        String acao,     
        String detalhes,
        String erro
) {}