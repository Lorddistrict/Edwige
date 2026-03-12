package io.realmit.edwige.api.shared.interfaces;

public interface ControllerInterface <R extends ResponseInterface2<R>, T extends RequestInterface2<T>> {

    R buildResponse(T request);
}
