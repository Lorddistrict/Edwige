package io.realmit.edwige.api.services.interfaces;

public interface RequestHandlerServiceInterface<R> {
    void handleRequest(R request);
}
