package org.movies;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;


@Entity
public class Ator extends PanacheEntity {

    @NotNull
    @Size(min = 2)
    public String nome;

    @Enumerated(EnumType.STRING)
    @NotNull
    public Genero genero;

    // @ManyToMany(mappedBy = "atores")
    //public List<Filmes> filmes;
}