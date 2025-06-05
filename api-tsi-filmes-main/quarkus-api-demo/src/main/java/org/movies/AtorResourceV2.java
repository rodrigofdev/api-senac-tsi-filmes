package org.movies;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("v2/atores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtorResourceV2 {

    @GET
    public List<AtorDTO> listarTodos(@HeaderParam("X-API-Key") String chave) {
        AuthUtils.validarChave(chave); //  Validação da chave

        @SuppressWarnings("unchecked")
        List<Ator> atores = (List<Ator>) (List<?>) Ator.listAll();

        return atores.stream()
                .map(ator -> new AtorDTO(
                        ator.nome,
                        ator.genero.name(),
                        "Esta resposta é da versão 2"
                ))
                .toList();
    }
}
