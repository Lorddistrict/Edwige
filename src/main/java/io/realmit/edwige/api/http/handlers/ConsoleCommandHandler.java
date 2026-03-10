package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.ConsoleCommandController;
import io.realmit.edwige.api.dto.requests.console.CommandRequest;
import io.realmit.edwige.api.dto.requests.console.ConsoleCommandRequest;
import io.realmit.edwige.api.dto.requests.console.enums.OnExpire;
import io.realmit.edwige.api.dto.requests.console.enums.RunAsEnum;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.HttpUtils;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.*;

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

        if (!validateRequest(exchange, request)) return;
        controller.handleRequest(request);

        sendJsonMessage(exchange, HttpStatus.HTTP_OK, HttpStatus.HTTP_OK.reason());
    }

    private boolean validateRequest(HttpExchange exchange, ConsoleCommandRequest request) throws IOException {
        String message;

        // Validate commands list is not null or empty
        if (request.commands() == null || request.commands().isEmpty()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Commands list cannot be null or empty");
            return false;
        }

        // Validate each command in the list
        for (int i = 0; i < request.commands().size(); i++) {
            CommandRequest commandRequest = request.commands().get(i);

            // Validate command string is not null/blank
            if (commandRequest.command() == null || commandRequest.command().isBlank()) {
                message = "Command at index " + i + " cannot be null or blank";
                sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, message);
                return false;
            }

            // Validate runAs enum if provided (must be CONSOLE or PLAYER)
            if (commandRequest.runAs() != null) {
                try {
                    RunAsEnum.valueOf(commandRequest.runAs().name());
                } catch (IllegalArgumentException e) {
                    message = "Invalid runAs value at index " + i + ": " + commandRequest.runAs();
                    sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, message);
                    return false;
                }
            }

            // If runAs is PLAYER, targetPlayer should be provided
            if (commandRequest.runAs() == RunAsEnum.PLAYER && commandRequest.targetPlayer() == null) {
                message = "targetPlayer is required when runAs is PLAYER at index " + i;
                sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, message);
                return false;
            }

            // If waitForPlayer is true, targetPlayer should be provided
            if (Boolean.TRUE.equals(commandRequest.waitForPlayer()) && commandRequest.targetPlayer() == null) {
                message = "targetPlayer is required when waitForPlayer is true at index " + i;
                sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, message);
                return false;
            }
        }

        // Validate onExpire enum if provided
        if (request.onExpire() != null) {
            try {
                OnExpire.valueOf(request.onExpire().name());
            } catch (IllegalArgumentException e) {
                message = "Invalid onExpire value: " + request.onExpire();
                sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, message);
                return false;
            }
        }

        // Validate ttlSeconds if provided (must be positive)
        if (request.ttlSeconds() != null && request.ttlSeconds() <= 0) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "ttlSeconds must be positive");
            return false;
        }

        return true;
    }
}

