package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.interfaces.ControllerInterface;
import io.realmit.edwige.api.dto.requests.console.ConsoleCommandRequest;
import io.realmit.edwige.api.dto.responses.console.CommandResponse;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.HttpUtils;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;
import io.realmit.edwige.api.validators.AbstractValidator;
import io.realmit.edwige.api.validators.console.ConsoleCommandValidator;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.*;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJson;

public class ConsoleCommandHandler implements HttpHandler {

    private final AbstractValidator<ConsoleCommandRequest> validator = new ConsoleCommandValidator();
    private final ControllerInterface<CommandResponse, ConsoleCommandRequest> controller;
    private final String bearerToken;

    public ConsoleCommandHandler(
            ControllerInterface<CommandResponse, ConsoleCommandRequest> controller,
            String bearerToken
    ) {
        this.controller = controller;
        this.bearerToken = bearerToken;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            sendJsonMessage(exchange, HttpStatus.HTTP_METHOD_NOT_ALLOWED, HttpStatus.HTTP_METHOD_NOT_ALLOWED.reason());
            return;
        }

        if (!HttpUtils.validateBearerToken(exchange, bearerToken)) return;

        String contentType = exchange.getRequestHeaders().getFirst(HEADER_KEY_CONTENT_TYPE);

        if (contentType == null || !contentType.startsWith(HEADER_VALUE_JSON)) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        ConsoleCommandRequest request;

        try {
            request = JsonMapper.fromJson(JsonUtils.readBody(exchange), ConsoleCommandRequest.class);
        } catch (IllegalArgumentException e) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        if (!validator.isValid(exchange, request)) return;
        CommandResponse response = controller.buildResponse(request);
        sendJson(exchange, HttpStatus.HTTP_OK, JsonMapper.toJson(response));
    }
}

