package com.skillswap.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillswap.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * Thin wrapper around Ollama's local REST API (https://github.com/ollama/ollama/blob/main/docs/api.md).
 * Every AI feature in this app goes through here — this is the ONLY place
 * that knows how to talk to Ollama. Swapping the model is a one-line config
 * change (app.ai.ollama.model / OLLAMA_MODEL env var); nothing here changes.
 *
 * Deliberately dependency-free (no LangChain4j, no Spring AI) to keep the
 * moving parts obvious and easy to read for anyone reviewing this codebase.
 */
@Service
public class OllamaClient {

    private final String baseUrl;
    private final String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // LLM generations can be slow on modest hardware — give it real room
    // rather than failing fast on something that's just thinking.
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public OllamaClient(
            @Value("${app.ai.ollama.base-url}") String baseUrl,
            @Value("${app.ai.ollama.model}") String model) {
        this.baseUrl = baseUrl;
        this.model = model;
    }

    /**
     * Sends a single-turn prompt (with an optional system instruction) and
     * returns the model's plain-text reply.
     */
    public String generate(String systemPrompt, String userPrompt) {
        try {
            Map<String, Object> body = Map.of(
                    "model", model,
                    "system", systemPrompt == null ? "" : systemPrompt,
                    "prompt", userPrompt,
                    "stream", false
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/generate"))
                    .timeout(Duration.ofSeconds(120))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException(
                        "Local AI model returned an error. Is '" + model + "' pulled? (ollama pull " + model + ")",
                        HttpStatus.BAD_GATEWAY
                );
            }

            JsonNode json = objectMapper.readTree(response.body());
            JsonNode responseNode = json.get("response");
            return responseNode != null ? responseNode.asText().trim() : "";

        } catch (java.net.ConnectException e) {
            throw new ApiException(
                    "Couldn't reach the local AI model at " + baseUrl + ". Make sure Ollama is running (`ollama serve`) " +
                            "and the model is pulled (`ollama pull " + model + "`). See the README's AI Setup section.",
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new ApiException("AI request failed: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
