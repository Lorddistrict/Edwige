package io.realmit.edwige.api.http.utils;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class HttpUtils {

    public static boolean validateRequestMethod(HttpExchange exchange, String method) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            sendText(exchange, HttpStatus.HTTP_METHOD_NOT_ALLOWED.code(), HttpStatus.HTTP_METHOD_NOT_ALLOWED.reason());

            return false;
        }

        return true;
    }

    public static void sendText(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");

        if (body == null) {
            exchange.sendResponseHeaders(status, -1);
            exchange.getResponseBody().close();
            return;
        }

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
