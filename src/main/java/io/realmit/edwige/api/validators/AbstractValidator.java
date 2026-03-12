package io.realmit.edwige.api.validators;

import com.sun.net.httpserver.HttpExchange;
import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface2;
import io.realmit.edwige.api.dto.responses.ValidationErrorResponse;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.JsonMapper;
import io.realmit.edwige.api.validators.interfaces.ValidatorInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.realmit.edwige.api.http.utils.JsonUtils.sendJson;

public abstract class AbstractValidator<T extends RequestInterface2<T>> implements ValidatorInterface<T> {

    private final List<String> errors = new ArrayList<>();

    protected abstract void validate(T request);

    @Override
    public boolean isValid(HttpExchange exchange, T request) throws IOException {
        errors.clear();
        validate(request);

        if (!errors.isEmpty()) {
            String json = JsonMapper.toJson(new ValidationErrorResponse(List.copyOf(errors)));
            sendJson(exchange, HttpStatus.HTTP_BAD_REQUEST, json);
            return false;
        }

        return true;
    }

    protected void reject(String message) {
        errors.add(message);
    }

    protected boolean hasErrors() {
        return !errors.isEmpty();
    }
}
