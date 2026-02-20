package io.realmit.edwige.api.http.utils;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class JsonUtils {

    public static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_VALUE_JSON = "application/json";
    public static final String HEADER_VALUE_CHARSET_UTF8 = "charset=utf-8";
    public static final String HEADER_VALUE_JSON_CHARSET_UTF8 = HEADER_VALUE_JSON+"; "+HEADER_VALUE_CHARSET_UTF8;

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            in.transferTo(out);

            return out.toString(StandardCharsets.UTF_8);
        }
    }

    public static void sendJson(HttpExchange exchange, HttpStatus status, String jsonBody) throws IOException {
        byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON_CHARSET_UTF8);
        exchange.sendResponseHeaders(status.code(), bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendJsonMessage(HttpExchange exchange, HttpStatus status, String message) throws IOException {
        String safeMessage = escapeForJsonString(message);
        String json = "{\"code\":" + status.code() + ",\"message\":\"" + safeMessage + "\"}";
        sendJson(exchange, status, json);
    }

    private static String escapeForJsonString(String message) {
        if (message == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(message.length() + 16);

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            switch (c) {
                case '\\' -> sb.append("\\\\");
                case '"'  -> sb.append("\\\"");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }
}
