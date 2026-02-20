package io.realmit.edwige.api.dto.responses;

import io.realmit.edwige.api.dto.requests.interfaces.ResponseInterface;

public record WebsiteRegistrationResponse(
        boolean success
) implements ResponseInterface<WebsiteRegistrationResponse> {}
