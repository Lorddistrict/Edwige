package io.realmit.edwige.api.dto.requests;

public record WebsiteRegistrationRequest(
        String username,
        String email,
        String ip,
        String callbackUrl
) {
}
