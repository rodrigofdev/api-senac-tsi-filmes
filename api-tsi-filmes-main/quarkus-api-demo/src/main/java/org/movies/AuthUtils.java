package org.movies;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;

public class AuthUtils {

    public static void validarChave(String chave) {
        if (chave == null || chave.isBlank()) {
            throw new WebApplicationException("Cabeçalho X-API-Key ausente", Response.Status.UNAUTHORIZED);
        }

        ApiKey apiKey = ApiKey.find("chave = ?1 AND ativo = true", chave).firstResult();
        if (apiKey == null) {
            throw new WebApplicationException("Chave de API inválida", Response.Status.FORBIDDEN);
        }
    }
}
