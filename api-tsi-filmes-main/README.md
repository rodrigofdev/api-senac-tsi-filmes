# 🎬 Projeto API de Filmes com Quarkus

Este projeto é uma API REST desenvolvida com [Quarkus](https://quarkus.io/) que gerencia informações sobre filmes, diretores e atores. A aplicação utiliza Hibernate ORM com Panache, Jakarta REST (JAX-RS) e validação com Bean Validation (Jakarta Validation).

---

## 📦 Tecnologias Utilizadas

- Java 17+
- [Quarkus](https://quarkus.io/)
- Hibernate ORM com Panache
- Jakarta Persistence (JPA)
- Jakarta REST (JAX-RS)
- Jakarta Bean Validation
- Banco de dados relacional (H2, PostgreSQL, etc.)

---


---

## 🧑‍🎤 Entidades

### 🎭 Ator
- `id`: Long (herdado de `PanacheEntity`)
- `nome`: String (mínimo 2 caracteres)
- `genero`: Enum (`MASCULINO`, `FEMININO`, `OUTRO`)

### 🎬 Diretor
- `id`: Long (herdado de `PanacheEntity`)
- `nome`: String

### 🎞️ Filmes
- `id`: Long (herdado de `PanacheEntity`)
- `nome`: String
- `genero`: Enum

---

## 🌐 Endpoints da API

### `/atores`

| Método | Endpoint        | Descrição                   |
|--------|------------------|-----------------------------|
| GET    | `/atores`        | Lista todos os atores       |
| GET    | `/atores/{id}`   | Retorna ator por ID         |
| POST   | `/atores`        | Cria um novo ator           |
| PUT    | `/atores/{id}`   | Atualiza ator existente     |
| DELETE | `/atores/{id}`   | Remove um ator por ID       |

---

### `/diretores`

| Método | Endpoint           | Descrição                     |
|--------|---------------------|-------------------------------|
| GET    | `/diretores`        | Lista todos os diretores      |
| GET    | `/diretores/{id}`   | Retorna diretor por ID        |
| POST   | `/diretores`        | Cria um novo diretor          |
| PUT    | `/diretores/{id}`   | Atualiza diretor existente    |
| DELETE | `/diretores/{id}`   | Remove um diretor por ID      |

---

### `/filmes`

| Método | Endpoint         | Descrição                    |
|--------|------------------|------------------------------|
| GET    | `/filmes`        | Lista todos os filmes        |
| GET    | `/filmes/{id}`   | Retorna um filme por ID      |
| POST   | `/filmes`        | Cria um novo filme           |
| PUT    | `/filmes/{id}`   | Atualiza um filme existente  |
| DELETE | `/filmes/{id}`   | Remove um filme por ID       |

---

## 🧪 Como Executar o Projeto

### Requisitos

- Java 17+
- Maven

### Executar em modo dev

```bash
./mvnw quarkus:dev

-- 
📌 Exemplos de Requisições
Criar um novo ator:

bash

curl -X POST http://localhost:8080/atores \
     -H "Content-Type: application/json" \
     -d '{"nome": "Keanu Reeves", "genero": "MASCULINO"}'
Criar um novo filme:

bash

curl -X POST http://localhost:8080/filmes \
     -H "Content-Type: application/json" \
     -d '{"nome": "Matrix", "genero": "FICCAO", "diretor": {"id": 1}, "atores": [{"id": 1}]}'


