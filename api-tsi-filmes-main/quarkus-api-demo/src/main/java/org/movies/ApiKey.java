package org.movies;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class ApiKey extends PanacheEntity {

    @NotBlank
    public String usuario;

    @NotBlank
    public String chave;

    public boolean ativo = true;
}
