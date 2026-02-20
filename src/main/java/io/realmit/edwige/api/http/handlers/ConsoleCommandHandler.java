package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.ConsoleCommandController;
import io.realmit.edwige.api.dto.requests.ConsoleCommandRequest;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.HttpUtils;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.HEADER_KEY_CONTENT_TYPE;
import static io.realmit.edwige.api.http.utils.JsonUtils.HEADER_VALUE_JSON;

public class ConsoleCommandHandler implements HttpHandler {

    private final ConsoleCommandController controller;
    private final String bearerToken;

    public ConsoleCommandHandler(
            ConsoleCommandController controller,
            String bearerToken
    ) {
        this.controller = controller;
        this.bearerToken = bearerToken;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            return;
        }

        if (!HttpUtils.validateBearerToken(exchange, bearerToken)) {
            return;
        }

        String contentType = exchange.getRequestHeaders().getFirst(HEADER_KEY_CONTENT_TYPE);

        if (contentType == null || !contentType.startsWith(HEADER_VALUE_JSON)) {
            exchange.sendResponseHeaders(HttpStatus.HTTP_BAD_REQUEST.code(), -1);
            return;
        }

        ConsoleCommandRequest request;

        try {
            request = JsonMapper.fromJson(JsonUtils.readBody(exchange), ConsoleCommandRequest.class);
        } catch (IllegalArgumentException e) {
            exchange.sendResponseHeaders(HttpStatus.HTTP_BAD_REQUEST.code(), -1);
            return;
        }

        controller.handleRequest(request);

        byte[] response = "OK".getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(HttpStatus.HTTP_OK.code(), response.length);
        exchange.getResponseBody().write(response);
        exchange.getResponseBody().close();
    }
}

