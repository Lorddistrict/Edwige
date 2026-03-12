package io.realmit.edwige.api.controllers.interfaces;

import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface2;
import io.realmit.edwige.api.dto.responses.interfaces.ResponseInterface2;

public interface ControllerInterface <R extends ResponseInterface2<R>, T extends RequestInterface2<T>> {

    R buildResponse(T request);
}
