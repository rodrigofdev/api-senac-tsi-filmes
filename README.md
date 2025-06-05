

# 🎬 Projeto API de Filmes com Quarkus

Este projeto é uma API RESTful desenvolvida com [Quarkus](https://quarkus.io/) que gerencia informações sobre filmes, diretores e atores. A aplicação utiliza Hibernate ORM com Panache, Jakarta REST (JAX-RS), validação com Bean Validation, autenticação via API Key, versionamento e outros recursos avançados de APIs RESTful.

---

## 📦 Tecnologias Utilizadas

* Java 17+
* Quarkus
* Hibernate ORM com Panache
* Jakarta Persistence (JPA)
* Jakarta REST (JAX-RS)
* Jakarta Bean Validation
* Banco de dados H2 (em memória)
* Maven
* Swagger UI
* Thunder Client / Postman

---

## 🧑‍🎤 Entidades

### 🎭 Ator

* `id`: Long (herdado de `PanacheEntity`)
* `nome`: String (mínimo 2 caracteres)
* `genero`: Enum (`MASCULINO`, `FEMININO`, `OUTRO`)

### 🎬 Diretor

* `id`: Long
* `nome`: String (mínimo 2 caracteres)

### 🎞️ Filmes

* `id`: Long
* `nome`: String
* `descricao`: String
* `categoria`: Enum
* `anoLancamento`: Integer
* `diretor`: relacionamento `ManyToOne`
* `atores`: relacionamento `ManyToMany`

---

## 🌐 Endpoints REST

### `/atores`, `/diretores`, `/filmes`

| Método | Endpoint           | Descrição          |
| ------ | ------------------ | ------------------ |
| GET    | `/[entidade]`      | Lista todos        |
| GET    | `/[entidade]/{id}` | Busca por ID       |
| POST   | `/[entidade]`      | Cria um novo       |
| PUT    | `/[entidade]/{id}` | Atualiza existente |
| DELETE | `/[entidade]/{id}` | Remove por ID      |

---

## 🚀 Recursos Avançados

### 🔐 1. Autenticação com API Key

* Gere uma chave via:

  ```
  POST /api-keys
  Body: { "usuario": "admin" }
  ```
* Use em todas as requisições protegidas:

  ```
  Header: X-API-Key: sua-chave
  ```

---

### 🧠 2. Idempotência (POST seguro)

* Envie o header:

  ```
  Idempotency-Key: valor-unico
  ```
* Evita que POSTs dupliquem dados em requisições repetidas

---

### 📈 3. Rate Limiting

* Limite de **5 requisições por minuto por chave**
* Excedeu? Resposta `429 Too Many Requests`
* Headers informativos:

  * `X-RateLimit-Limit`
  * `X-RateLimit-Remaining`
  * `X-RateLimit-Reset`

---

### 🌐 4. CORS

Configurado em `application.properties`:

```properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000
quarkus.http.cors.headers=Content-Type,X-API-Key,Idempotency-Key
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
quarkus.http.cors.exposed-headers=X-RateLimit-Limit,X-RateLimit-Remaining,X-RateLimit-Reset
```

---

### 🔀 5. Versionamento de API

* Versão atual: `/atores`
* Versão 2: `/api/v2/atores`
  → retorna `AtorDTO` com campo adicional `"infoVersao"`

---

### 🛡️ 6. Validação e Tratamento de Erros

* Anotações como `@NotNull`, `@Size`, etc. nas entidades
* Uso de `@Valid` nos métodos do Resource
* Handler global `GlobalExceptionHandler`:

  * Responde `400` com mensagens de erro de campos
  * Responde `500` com fallback genérico

---

## 🧪 Como Executar o Projeto

### Pré-requisitos

* Java 17+
* Maven

### Rodar localmente

```bash
./mvnw quarkus:dev
```

---

## 📌 Exemplos de Requisições

### Criar ator

```bash
curl -X POST http://localhost:8080/atores \
     -H "Content-Type: application/json" \
     -H "X-API-Key: sua-chave" \
     -H "Idempotency-Key: chave-unica-123" \
     -d '{"nome": "Keanu Reeves", "genero": "MASCULINO"}'
```

### Criar filme

```bash
curl -X POST http://localhost:8080/filmes \
     -H "Content-Type: application/json" \
     -H "X-API-Key: sua-chave" \
     -H "Idempotency-Key: chave-unica-456" \
     -d '{
           "nome": "Matrix",
           "descricao": "Ficção científica",
           "anoLancamento": 1999,
           "categoria": "FICCAO",
           "diretor": { "id": 1 },
           "atores": [{ "id": 1 }]
         }'
```
