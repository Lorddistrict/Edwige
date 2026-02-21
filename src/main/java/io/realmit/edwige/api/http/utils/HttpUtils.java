package io.realmit.edwige.api.http.utils;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.http.enums.HttpStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;

import static io.realmit.edwige.api.http.utils.JsonUtils.sendJsonMessage;

public final class HttpUtils {

    public static final String HEADER_KEY_AUTHORIZATION = "Authorization";

    public static boolean validateRequestMethod(HttpExchange exchange, String method) {
        return exchange.getRequestMethod().equalsIgnoreCase(method);
    }

    public static Optional<String> extractBearerToken(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst(HEADER_KEY_AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Optional.empty();
        }

        // Remove "Bearer " prefix
        return Optional.of(authHeader.substring(7));
    }

    public static boolean validateBearerToken(HttpExchange exchange, String expectedToken) throws IOException {
        Optional<String> token = extractBearerToken(exchange);

        if (token.isEmpty()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        if (!token.get().equals(expectedToken)) {
            sendJsonMessage(exchange, HttpStatus.HTTP_UNAUTHORIZED, "Invalid token");
            return false;
        }

        return true;
    }

    public static @Nullable String extractUrlParameterFromURL(HttpExchange exchange) {
        String contextPath = exchange.getHttpContext().getPath();
        String fullPath = exchange.getRequestURI().getPath();
        String rest = fullPath.substring(contextPath.length());
        String parameter = null;

        if (!rest.isEmpty()) {
            if (rest.startsWith("/")) {
                rest = rest.substring(1);
            }

            String[] segments = rest.split("/");
            parameter = segments[0];
        }

        if (parameter == null || parameter.isBlank()) {
            return null;
        }

        return parameter;
    }
}
