package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.info.InfoController;
import io.realmit.edwige.api.dto.responses.info.InfoResponse;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;
import io.realmit.edwige.api.http.utils.HttpUtils;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJson;

public final class InfoHandler implements HttpHandler {

    private final InfoController controller;
    private final String bearerToken;

    public InfoHandler(
            InfoController controller,
            String bearerToken
    ) {
        this.controller = controller;
        this.bearerToken = bearerToken;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_GET.method())) {
            return;
        }

        if (!HttpUtils.validateBearerToken(exchange, bearerToken)) {
            return;
        }

        InfoResponse infoResponse = controller.buildResponse();

        sendJson(exchange, HttpStatus.HTTP_OK, """
        {
          "playerCount": %d,
          "offlineCount": %d,
          "maxPlayers": %s,
          "serverFull": %s,
          "serverVersion": %s,
          "onlinePlayers": %s,
          "offlinePlayers": %s
        }
        """.formatted(
                infoResponse.onlineCount(),
                infoResponse.offlineCount(),
                infoResponse.maxPlayers(),
                infoResponse.serverFull(),
                infoResponse.serverVersion(),
                infoResponse.onlinePlayers(),
                infoResponse.offlinePlayers()
        ));
    }
}
