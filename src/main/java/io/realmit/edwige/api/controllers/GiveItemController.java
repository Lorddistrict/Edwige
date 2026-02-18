package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.dto.requests.GiveItemRequest;
import io.realmit.edwige.api.services.GiveItemService;

final public class GiveItemController {

    final private GiveItemService giveItemService;

    public GiveItemController(GiveItemService giveItemService) {
        this.giveItemService = giveItemService;
    }

    public String handle(GiveItemRequest request) {
        giveItemService.giveItem(request);

        return "Giving " + request.amount() + " " + request.item() + " to " + request.player();
    }
}
