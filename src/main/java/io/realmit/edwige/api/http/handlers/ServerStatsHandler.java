package io.realmit.edwige.api.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.realmit.edwige.api.controllers.ServerStatsController;
import io.realmit.edwige.api.dto.responses.ServerStatsResponse;
import io.realmit.edwige.api.http.enums.HttpMethods;
import io.realmit.edwige.api.http.enums.HttpStatus;

import java.io.IOException;

import static io.realmit.edwige.api.http.utils.HttpRequestUtils.validateRequestMethod;
import static io.realmit.edwige.api.http.utils.HttpResponseUtils.sendJson;

final public class ServerStatsHandler implements HttpHandler {

    final private ServerStatsController controller;

    public ServerStatsHandler(ServerStatsController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!validateRequestMethod(exchange, HttpMethods.HTTP_GET.method())) {
            return;
        }

        ServerStatsResponse serverStatsResponse = controller.handle();

        String json = """
        {
          "onlineCount": %d,
          "offlineCount": %d,
          "maxPlayers": %s,
          "serverFull": %s,
          "onlinePlayers": %s,
          "offlinePlayers": %s
        }
        """.formatted(
                serverStatsResponse.onlineCount(),
                serverStatsResponse.offlineCount(),
                serverStatsResponse.maxPlayers(),
                serverStatsResponse.serverFull(),
                serverStatsResponse.onlinePlayers(),
                serverStatsResponse.offlinePlayers()
        );

        sendJson(exchange, HttpStatus.HTTP_OK.code(), json);
    }
}
