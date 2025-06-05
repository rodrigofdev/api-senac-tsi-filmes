package org.movies;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/filmes")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmesResource {

    @GET
    public List<Filmes> listarTodos(@HeaderParam("X-API-Key") String chave) {
        AuthUtils.validarChave(chave); // 🔐 Proteção
        return Filmes.listAll();
    }

    @GET
    @Path("/{id}")
    public Filmes buscarPorId(
            @PathParam("id") Long id,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // 🔐 Proteção
        return Filmes.findById(id);
    }

    @POST
    @Transactional
    public Response criar(
            Filmes filme,
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // 🔐 Proteção

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return Response.status(400).entity("Cabeçalho 'Idempotency-Key' é obrigatório.").build();
        }

        boolean chaveUsada = IdempotencyKey.find("chave = ?1", idempotencyKey)
                .firstResultOptional().isPresent();
        if (chaveUsada) {
            return Response.status(409).entity("Requisição duplicada. Essa chave já foi usada.").build();
        }

        filme.persist();

        IdempotencyKey registro = new IdempotencyKey();
        registro.chave = idempotencyKey;
        registro.metodo = "POST";
        registro.endpoint = "/filmes"; // ✅ Corrigido
        registro.persist();

        return Response.status(Response.Status.CREATED).entity(filme).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Filmes atualizar(
            @PathParam("id") Long id,
            Filmes filme,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // 🔐 Proteção

        Filmes entidade = Filmes.findById(id);
        if (entidade == null) {
            throw new NotFoundException("Filme não encontrado com o ID: " + id);
        }

        entidade.nome = filme.nome;
        entidade.descricao = filme.descricao;
        entidade.categoria = filme.categoria;
        entidade.anoLancamento = filme.anoLancamento;

        return entidade;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluir(
            @PathParam("id") Long id,
            @HeaderParam("X-API-Key") String chave
    ) {
        AuthUtils.validarChave(chave); // 🔐 Proteção

        boolean excluido = Filmes.deleteById(id);
        if (excluido) {
            return Response.noContent().build();
        }
        throw new NotFoundException("Filme não encontrado com o ID: " + id);
    }
}
