package io.realmit.edwige.api.dto.responses.validateRegistration;

import io.realmit.edwige.api.dto.requests.interfaces.ResponseInterface;

public record ValidateRegistrationResponse(
        boolean success
) implements ResponseInterface<ValidateRegistrationResponse> {}
