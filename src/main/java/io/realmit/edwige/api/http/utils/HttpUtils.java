package io.realmit.edwige.api.http.utils;

import com.sun.net.httpserver.HttpExchange;

public final class HttpUtils {

    public static boolean validateRequestMethod(HttpExchange exchange, String method) {
        return exchange.getRequestMethod().equalsIgnoreCase(method);
    }
}
