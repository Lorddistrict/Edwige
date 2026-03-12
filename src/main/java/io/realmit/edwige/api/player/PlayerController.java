package io.realmit.edwige.api.player;

import io.realmit.edwige.api.controllers.interfaces.ResponseBuilderControllerInterface;
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
