package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.InfoController;
import io.realmit.edwige.api.dto.responses.InfoResponse;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.JsonUtils.sendJson;

public final class InfoHandler implements HttpHandler {

    private final InfoController controller;

    public InfoHandler(InfoController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_GET.method())) {
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
