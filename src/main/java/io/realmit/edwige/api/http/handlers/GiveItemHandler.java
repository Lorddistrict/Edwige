package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controller.GiveItemController;
import io.realmit.edwige.api.dto.request.GiveItemRequest;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.realmit.edwige.api.http.utils.HttpResponseUtils.sendText;
import static io.realmit.edwige.api.http.utils.HttpRequestUtils.validateRequestMethod;

final public class GiveItemHandler implements HttpHandler {

    final private GiveItemController controller;

    public GiveItemHandler(GiveItemController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_POST.method())) {
            return;
        }

        String body = readBody(exchange);
        String playerName = extractJsonValue(body, "player");
        String itemName = extractJsonValue(body, "item");
        String amountStr = extractJsonValue(body, "amount");

        if (playerName == null || itemName == null || amountStr == null) {
            sendText(exchange, HttpStatus.HTTP_BAD_REQUEST.code(), HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            sendText(exchange, HttpStatus.HTTP_BAD_REQUEST.code(), HttpStatus.HTTP_BAD_REQUEST.reason());
            return;
        }

        GiveItemRequest request = new GiveItemRequest(playerName, itemName, amount);
        String message = controller.handle(request);

        sendText(exchange, HttpStatus.HTTP_OK.code(), message);
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            in.transferTo(out);
            return out.toString(StandardCharsets.UTF_8);
        }
    }

    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"";
        int i = json.indexOf(pattern);
        if (i == -1) return null;
        int colon = json.indexOf(':', i + pattern.length());
        if (colon == -1) return null;

        int pos = colon + 1;
        while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }

        if (pos < json.length() && json.charAt(pos) == '"') {
            int secondQuote = json.indexOf('"', pos + 1);
            if (secondQuote == -1) return null;
            return json.substring(pos + 1, secondQuote);
        } else {
            int end = pos;
            while (end < json.length()
                    && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
                end++;
            }
            if (end == pos) return null;
            return json.substring(pos, end);
        }
    }
}
