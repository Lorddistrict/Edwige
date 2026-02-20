package io.realmit.edwige.api.dto.requests;

import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface;

public record WebsiteRegistrationRequest(
        String username,
        String email,
        String ip,
        int timeout,
        String callbackUrl
) implements RequestInterface<WebsiteRegistrationRequest> {}
