package org.movies;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/atores")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorResource {

    @GET
    public List<Ator> listarTodos(@HeaderParam("X-API-Key") String chave){
        AuthUtils.validarChave(chave); // Validação da chave
        return Ator.listAll();
    }

    @GET
    @Path("/{id}")
    public Ator buscarPorId(@PathParam("id") Long id, @HeaderParam("X-API-Key") String chave) {
        AuthUtils.validarChave(chave); // Validação da chave
        return Ator.findById(id);
    }

    @POST
    @Transactional
    public Response criar(
            Ator ator,
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // Validação da chave

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Response.status(400).entity("Cabeçalho 'Idempotency-Key' é obrigatório.").build();
        }

        boolean chaveUsada = IdempotencyKey.find("chave = ?1", idempotencyKey)
                .firstResultOptional().isPresent();
        if (chaveUsada) {
            return Response.status(409).entity("Requisição duplicada. Essa chave já foi usada.").build();
        }

        ator.persist();

        IdempotencyKey registro = new IdempotencyKey();
        registro.chave = idempotencyKey;
        registro.metodo = "POST";
        registro.endpoint = "/atores";
        registro.persist();

        return Response.status(Response.Status.CREATED).entity(ator).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(
            @PathParam("id") Long id,
            Ator atorAtualizado,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // Validação da chave

        Ator atorExistente = Ator.findById(id);
        if (atorExistente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        atorExistente.nome = atorAtualizado.nome;
        atorExistente.genero = atorAtualizado.genero;

        return Response.ok(atorExistente).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluir(
            @PathParam("id") Long id,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); //Validação da chave

        boolean excluido = Ator.deleteById(id);
        if (excluido) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
