package org.movies;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/api-keys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiKeyResource {

    public static class NovoUsuario {
        public String usuario;
    }

    @POST
    @Transactional
    public Response gerar(NovoUsuario novo) {
        if (novo.usuario == null || novo.usuario.isBlank()) {
            return Response.status(400).entity("Usuário é obrigatório").build();
        }

        ApiKey apiKey = new ApiKey();
        apiKey.usuario = novo.usuario;
        apiKey.chave = UUID.randomUUID().toString();
        apiKey.ativo = true;
        apiKey.persist();

        return Response.status(201).entity(apiKey).build();
    }
}
