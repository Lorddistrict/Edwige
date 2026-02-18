package io.realmit.edwige.api.controller;

import io.realmit.edwige.api.dto.GiveItemRequest;
import io.realmit.edwige.api.service.GiveItemService;

public final class GiveItemController {

    private final GiveItemService giveItemService;

    public GiveItemController(GiveItemService giveItemService) {
        this.giveItemService = giveItemService;
    }

    public String handle(GiveItemRequest request) {
        giveItemService.giveItem(request);

        return "Giving " + request.amount() + " " + request.item() + " to " + request.player();
    }
}
