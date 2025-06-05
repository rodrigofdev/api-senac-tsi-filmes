package org.movies;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import java.util.List;


@Entity
public class Diretor extends PanacheEntity {

    @NotNull
    @Size(min = 2)
    public String nome;

   // @OneToMany(mappedBy = "diretor")
   // public List<Filmes> filmes;
}

