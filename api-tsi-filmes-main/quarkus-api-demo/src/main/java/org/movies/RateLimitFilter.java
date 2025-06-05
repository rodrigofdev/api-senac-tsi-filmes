package org.movies;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
@Priority(Priorities.AUTHORIZATION) // Executa depois da autenticação
public class RateLimitFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final int LIMIT = 5; // Limite de requisições por minuto
    private static final Map<String, Requisicao> requisicoes = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-API-Key");

        // Se a chave for ausente, outra parte (AuthUtils) cuida disso
        if (apiKey == null || apiKey.isBlank()) return;

        Requisicao req = requisicoes.getOrDefault(apiKey, new Requisicao(0, Instant.now()));
        Instant agora = Instant.now();

        // Se já passou o tempo de reset, reinicia contador
        if (agora.isAfter(req.resetTime)) {
            req = new Requisicao(0, agora.plusSeconds(60));
        }

        if (req.count >= LIMIT) {
            requestContext.abortWith(Response.status(429)
                    .header("X-RateLimit-Limit", LIMIT)
                    .header("X-RateLimit-Remaining", 0)
                    .header("X-RateLimit-Reset", req.resetTime.getEpochSecond())
                    .entity("Você excedeu o limite de requisições por minuto.")
                    .build());
            return;
        }

        // Atualiza o contador
        req.count++;
        requisicoes.put(apiKey, req);

        // Guarda para o ResponseFilter
        requestContext.setProperty("rate-limit-count", req.count);
        requestContext.setProperty("rate-limit-reset", req.resetTime.getEpochSecond());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String apiKey = requestContext.getHeaderString("X-API-Key");
        if (apiKey == null || !requisicoes.containsKey(apiKey)) return;

        Object countObj = requestContext.getProperty("rate-limit-count");
        Object resetObj = requestContext.getProperty("rate-limit-reset");

        if (countObj == null || resetObj == null) return;

        int count = (int) countObj;
        long reset = (long) resetObj;

        int remaining = Math.max(0, LIMIT - count);

        responseContext.getHeaders().add("X-RateLimit-Limit", LIMIT);
        responseContext.getHeaders().add("X-RateLimit-Remaining", remaining);
        responseContext.getHeaders().add("X-RateLimit-Reset", reset);
    }

    // Classe auxiliar
    static class Requisicao {
        int count;
        Instant resetTime;

        public Requisicao(int count, Instant resetTime) {
            this.count = count;
            this.resetTime = resetTime;
        }
    }
}
