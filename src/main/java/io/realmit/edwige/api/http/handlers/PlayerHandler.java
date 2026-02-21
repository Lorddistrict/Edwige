package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.PlayerController;
import io.realmit.edwige.api.dto.responses.PlayerResponse;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.HttpUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.extractUrlParameterFromURL;
import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJson;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJsonMessage;

public final class PlayerHandler implements HttpHandler {

    private final PlayerController controller;
    private final String bearerToken;

    public PlayerHandler(
            PlayerController controller,
            String bearerToken
    ) {
        this.controller = controller;
        this.bearerToken = bearerToken;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_GET.method())) {
            sendJsonMessage(exchange, HttpStatus.HTTP_METHOD_NOT_ALLOWED, HttpStatus.HTTP_METHOD_NOT_ALLOWED.reason());
            return;
        }

        if (!HttpUtils.validateBearerToken(exchange, bearerToken)) {
            sendJsonMessage(exchange, HttpStatus.HTTP_UNAUTHORIZED, HttpStatus.HTTP_UNAUTHORIZED.reason());
            return;
        }

        @Nullable String parameter = extractUrlParameterFromURL(exchange);

        if (parameter == null) {
            sendJsonMessage(exchange, HttpStatus.HTTP_BAD_REQUEST, "Missing parameter in path");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(parameter);

        if (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_NOT_FOUND, "This player does not exist");
            return;
        }

        Player player = offlinePlayer.getPlayer();

        if (player == null || !player.isConnected()) {
            sendJsonMessage(exchange, HttpStatus.HTTP_NOT_FOUND, "This player is offline");
            return;
        }

        PlayerResponse playerResponse = controller.buildResponse(player);

        sendJson(exchange, HttpStatus.HTTP_OK, """
        {
            "uuid": %s,
            "username": %s,
            "isOnline": %b,
            "displayName": %s,
            "ip": %s,
            "ping": %d,
            "world": %s,
            "balance": %f,
            "firstPlayed": %d,
            "lastPlayed": %d,
            "isBanned": %b,
            "isOp": %b
        }
        """.formatted(
                playerResponse.uuid(),
                playerResponse.username(),
                playerResponse.isOnline(),
                playerResponse.displayName(),
                playerResponse.ip(),
                playerResponse.ping(),
                playerResponse.world(),
                playerResponse.balance(),
                playerResponse.firstPlayed(),
                playerResponse.lastPlayed(),
                playerResponse.isBanned(),
                playerResponse.isOp()
        ));
    }
}
