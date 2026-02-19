package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.requests.WebsiteRegistrationController;
import io.realmit.edwige.api.dto.requests.WebsiteRegistrationRequest;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.http.utils.JsonUtils;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpRequestUtils.validateRequestMethod;

public class WebsiteRegistrationHandler implements HttpHandler {

    private final WebsiteRegistrationController controller;

    public WebsiteRegistrationHandler(WebsiteRegistrationController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            return;
        }

        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

        if (contentType == null || !contentType.startsWith("application/json")) {
            exchange.sendResponseHeaders(HttpStatus.HTTP_BAD_REQUEST.code(), -1);

            return;
        }

        WebsiteRegistrationRequest request;

        try {
            request = JsonMapper.fromJson(JsonUtils.readBody(exchange), WebsiteRegistrationRequest.class);
        } catch (IllegalArgumentException e) {
            exchange.sendResponseHeaders(HttpStatus.HTTP_BAD_REQUEST.code(), -1);

            return;
        }

        controller.handleRequest(request);

        exchange.sendResponseHeaders(HttpStatus.HTTP_OK.code(), -1);
        exchange.getResponseBody().close();
    }
}
