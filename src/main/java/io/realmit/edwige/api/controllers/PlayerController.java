package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.controllers.interfaces.ResponseBuilderControllerInterface;
import io.realmit.edwige.api.dto.responses.player.PlayerResponse;
import io.realmit.edwige.api.services.PlayerService;
import org.bukkit.entity.Player;

public final class PlayerController implements ResponseBuilderControllerInterface<PlayerResponse> {

    private final PlayerService service;

    public PlayerController(PlayerService service) {
        this.service = service;
    }

    @Override
    public PlayerResponse buildResponse() {
        throw new UnsupportedOperationException(this.getClass() + " requires a Player");
    }

    public PlayerResponse buildResponse(Player player) {
        return service.buildResponse(player);
    }
}
