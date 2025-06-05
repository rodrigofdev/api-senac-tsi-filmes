package org.movies;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;


@Entity
public class Filmes extends PanacheEntity {
    @NotBlank(message = "O nome do filme é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    public String nome;


    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres")
    public String descricao;

    @NotNull(message = "O ano de lançamento do filme é obrigatório")
    @Column(nullable = false)
    public Integer anoLancamento;

   // @ManyToOne
    //public Diretor diretor;

    //@ManyToMany(fetch = FetchType.EAGER)
    //private List<Ator> atores;


    // Data de criação do produto
    public java.time.LocalDateTime dataCriacao = java.time.LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @NotNull
    public Categoria categoria;


    // Utilizando Panache, não precisamos criar getters/setters
    // nem implementar operações CRUD básicas
}