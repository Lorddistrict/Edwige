package io.realmit.edwige.api.http.utils;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpResponseUtils.sendText;

final public class HttpRequestUtils {

    private HttpRequestUtils() {
    }

    public static boolean validateRequestMethod(HttpExchange exchange, String method) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            sendText(exchange, HttpStatus.HTTP_METHOD_NOT_ALLOWED.code(), HttpStatus.HTTP_METHOD_NOT_ALLOWED.reason());

            return false;
        }

        return true;
    }
}
