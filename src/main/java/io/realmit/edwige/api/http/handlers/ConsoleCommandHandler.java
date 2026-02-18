package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.requests.ConsoleCommandController;
import io.realmit.edwige.api.dto.requests.ConsoleCommandRequest;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.realmit.edwige.api.http.utils.HttpRequestUtils.validateRequestMethod;

public class ConsoleCommandHandler implements HttpHandler {

    private final ConsoleCommandController controller;

    public ConsoleCommandHandler(ConsoleCommandController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            return;
        }

        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

        if (null == contentType || !contentType.startsWith("application/json")) {
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

