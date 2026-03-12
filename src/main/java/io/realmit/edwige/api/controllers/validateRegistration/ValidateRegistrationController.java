package io.realmit.edwige.api.controllers.validateRegistration;

import io.realmit.edwige.api.controllers.interfaces.RequestHandlerControllerInterface;
import io.realmit.edwige.api.dto.requests.validateRegistration.ValidateRegistrationRequest;
import io.realmit.edwige.api.services.ValidateRegistrationService;

public final class ValidateRegistrationController implements RequestHandlerControllerInterface<ValidateRegistrationRequest> {

    private final ValidateRegistrationService service;

    public ValidateRegistrationController(ValidateRegistrationService service) {
        this.service = service;
    }

    public void handleRequest(ValidateRegistrationRequest request) {
        service.handleRequest(request);
    }
}
