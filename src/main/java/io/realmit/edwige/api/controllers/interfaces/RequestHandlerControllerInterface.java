package io.realmit.edwige.api.controllers.interfaces;

public interface RequestHandlerControllerInterface<R> {
    void handleRequest(R request);
}
