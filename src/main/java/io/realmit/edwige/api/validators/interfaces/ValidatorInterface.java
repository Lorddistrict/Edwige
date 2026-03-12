package io.realmit.edwige.api.validators.interfaces;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface2;

import java.io.IOException;

public interface ValidatorInterface <T extends RequestInterface2<T>> {

    boolean isValid(HttpExchange exchange, T request) throws IOException;
}
