package org.movies;

//import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import jakarta.enterprise.context.ApplicationScoped;
import org.movies.Filmes;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/filmes")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FilmesResource {

    @GET
    public List<Filmes> listarTodos() {
        return Filmes.listAll();
    }

    @GET
    @Path("/{id}")
    public Filmes buscarPorId(@PathParam("id") Long id) {
        return Filmes.findById(id);
    }

    @POST
    public Response criar(Filmes filme) {
        filme.persist();
        return Response.status(Response.Status.CREATED).entity(filme).build();
    }

    @PUT
    @Path("/{id}")
    public Filmes atualizar(@PathParam("id") Long id, Filmes filme) {
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
    public Response excluir(@PathParam("id") Long id) {
        boolean excluido = Filmes.deleteById(id);
        if (excluido) {
            return Response.noContent().build();
        }
        throw new NotFoundException("Filme não encontrado com o ID: " + id);
    }
}