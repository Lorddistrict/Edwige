package io.realmit.edwige.api.validateRegistration;

import io.realmit.edwige.api.controllers.interfaces.RequestHandlerControllerInterface;

public final class ValidateRegistrationController implements RequestHandlerControllerInterface<ValidateRegistrationRequest> {

    private final ValidateRegistrationService service;

    public ValidateRegistrationController(ValidateRegistrationService service) {
        this.service = service;
    }

    public void handleRequest(ValidateRegistrationRequest request) {
        service.handleRequest(request);
    }
}
