package io.realmit.edwige.api.validateRegistration;

import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface;

public record ValidateRegistrationRequest(
        String username,
        String email,
        String ip,
        int timeout,
        String callbackUrl
) implements RequestInterface<ValidateRegistrationRequest> {}
