package io.realmit.edwige.api.shared.abstracts;

import io.realmit.edwige.api.shared.interfaces.ControllerInterface;
import io.realmit.edwige.api.shared.interfaces.RequestInterface2;
import io.realmit.edwige.api.shared.interfaces.ResponseInterface2;

public abstract class AbstractController<R extends ResponseInterface2<R>, T extends RequestInterface2<T>> implements ControllerInterface<R, T> {
    @Override
    public abstract R buildResponse(T request);
}
