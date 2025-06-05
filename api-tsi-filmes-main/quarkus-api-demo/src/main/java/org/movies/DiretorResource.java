package org.movies;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/diretores")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiretorResource {

    @GET
    public List<Diretor> listarTodos(@HeaderParam("X-API-Key") String chave) {
        AuthUtils.validarChave(chave); //
        return Diretor.listAll();
    }

    @GET
    @Path("/{id}")
    public Diretor buscarPorId(
            @PathParam("id") Long id,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave);
        return Diretor.findById(id);
    }

    @POST
    @Transactional
    public Response criar(
            Diretor diretor,
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave);

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Cabeçalho 'Idempotency-Key' é obrigatório.")
                    .build();
        }

        boolean chaveUsada = IdempotencyKey.find("chave = ?1", idempotencyKey)
                .firstResultOptional().isPresent();
        if (chaveUsada) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Requisição duplicada. Essa chave já foi usada.")
                    .build();
        }

        IdempotencyKey registro = new IdempotencyKey();
        registro.chave = idempotencyKey;
        registro.metodo = "POST";
        registro.endpoint = "/diretores";
        registro.persist();

        diretor.persist();
        return Response.status(Response.Status.CREATED).entity(diretor).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(
            @PathParam("id") Long id,
            Diretor diretorAtualizado,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave);

        Diretor diretorExistente = Diretor.findById(id);
        if (diretorExistente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        diretorExistente.nome = diretorAtualizado.nome;
        return Response.ok(diretorExistente).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluir(
            @PathParam("id") Long id,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave);

        boolean excluido = Diretor.deleteById(id);
        if (excluido) {
            return Response.noContent().build();
        }

        throw new NotFoundException("Diretor não encontrado com o ID: " + id);
    }
}
