package io.realmit.edwige.api.shared.interfaces;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ValidatorInterface <T extends RequestInterface2<T>> {

    boolean isValid(HttpExchange exchange, T request) throws IOException;
}
