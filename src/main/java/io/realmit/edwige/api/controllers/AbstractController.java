package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.controllers.interfaces.ControllerInterface;
import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface2;
import io.realmit.edwige.api.dto.responses.interfaces.ResponseInterface2;

public abstract class AbstractController<R extends ResponseInterface2<R>, T extends RequestInterface2<T>> implements ControllerInterface<R, T> {
    @Override
    public abstract R buildResponse(T request);
}
