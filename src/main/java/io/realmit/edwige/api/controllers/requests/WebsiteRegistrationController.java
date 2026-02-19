package io.realmit.edwige.api.controllers.requests;

import io.realmit.edwige.api.dto.requests.WebsiteRegistrationRequest;
import io.realmit.edwige.api.services.WebsiteRegistrationService;

public final class WebsiteRegistrationController {

    private final WebsiteRegistrationService service;

    public WebsiteRegistrationController(WebsiteRegistrationService service) {
        this.service = service;
    }

    public void handleRequest(WebsiteRegistrationRequest request) {
        service.handleRequest(request);
    }
}
