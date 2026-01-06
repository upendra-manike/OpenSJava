package io.github.upendramanike.apishield;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Safe HTTP client wrapper for API calls.
 */
public final class ApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final Duration timeout;

    private ApiClient(String baseUrl, Duration timeout) {
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout)
                .build();
    }

    /**
     * Creates a new API client.
     *
     * @param baseUrl the base URL for API calls
     * @param timeout the request timeout
     * @return a new API client
     */
    public static ApiClient create(String baseUrl, Duration timeout) {
        return new ApiClient(baseUrl, timeout);
    }

    /**
     * Makes a GET request.
     *
     * @param path the API path
     * @return a CompletableFuture with the response body
     */
    public CompletableFuture<String> get(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .timeout(timeout)
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    /**
     * Makes a POST request.
     *
     * @param path the API path
     * @param body the request body
     * @param headers optional headers
     * @return a CompletableFuture with the response body
     */
    public CompletableFuture<String> post(String path, String body, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + path))
                .timeout(timeout)
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (headers != null) {
            headers.forEach(builder::header);
        }

        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}

