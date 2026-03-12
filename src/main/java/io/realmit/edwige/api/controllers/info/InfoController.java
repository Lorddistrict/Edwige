package io.realmit.edwige.api.controllers.info;

import io.realmit.edwige.api.controllers.interfaces.ResponseBuilderControllerInterface;
import io.realmit.edwige.api.dto.responses.info.InfoResponse;
import io.realmit.edwige.api.services.InfoService;

public final class InfoController implements ResponseBuilderControllerInterface<InfoResponse> {

    private final InfoService service;

    public InfoController(InfoService service) {
        this.service = service;
    }

    public InfoResponse buildResponse() {
        return service.buildResponse();
    }
}
