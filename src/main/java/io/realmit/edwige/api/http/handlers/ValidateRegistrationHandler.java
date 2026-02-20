package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.ValidateRegistrationController;
import io.realmit.edwige.api.dto.requests.ValidateRegistrationRequest;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJsonMessage;

public class ValidateRegistrationHandler implements HttpHandler {

    private final ValidateRegistrationController controller;

    public ValidateRegistrationHandler(ValidateRegistrationController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            sendJsonMessage(exchange, HttpStatus.HTTP_METHOD_NOT_ALLOWED, HttpStatus.HTTP_METHOD_NOT_ALLOWED.reason());
            return;
        }

        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

        if (contentType == null || !contentType.startsWith("application/json")) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        ValidateRegistrationRequest request;

        try {
            request = JsonMapper.fromJson(JsonUtils.readBody(exchange), ValidateRegistrationRequest.class);
        } catch (IllegalArgumentException e) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        validateRequest(exchange, request);
        controller.handleRequest(request);

        sendJsonMessage(exchange, HttpStatus.HTTP_OK, HttpStatus.HTTP_OK.reason());
    }

    private void validateRequest(HttpExchange exchange, ValidateRegistrationRequest request) throws IOException {
        if (request.username() == null) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Username cannot be null");
            return;
        }

        if (request.username().isBlank()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Username cannot be blank");
            return;
        }

        if (request.email() == null) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Email cannot be null");
            return;
        }

        if (request.email().isBlank()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Email cannot be blank");
            return;
        }

        if (request.ip() == null) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "IP cannot be null");
            return;
        }

        if (request.ip().isBlank()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "IP cannot be blank");
            return;
        }

        if (request.callbackUrl() == null) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "CallbackUrl cannot be null");
            return;
        }

        if (request.callbackUrl().isBlank()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "CallbackUrl cannot be blank");
            return;
        }

        if (request.timeout() <= 0) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Timeout should be greater than 0");
        }
    }
}
