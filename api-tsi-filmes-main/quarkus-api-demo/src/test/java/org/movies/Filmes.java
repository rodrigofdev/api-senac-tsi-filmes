package org.movies;

import jakarta.validation.constraints.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class Filmes extends PanacheEntity {
    @NotBlank(message = "O nome do filme é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    public String nome;

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres")
    public String descricao;

    public Double anoLancamento;
    public String categoria;


    // Utilizando Panache, não precisamos criar getters/setters
    // nem implementar operações CRUD básicas
}