package org.movies;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class IdempotencyKey extends PanacheEntity {

    @NotBlank
    public String chave;

    @NotBlank
    public String metodo;

    @NotBlank
    public String endpoint;

    public LocalDateTime criadoEm = LocalDateTime.now();
}
