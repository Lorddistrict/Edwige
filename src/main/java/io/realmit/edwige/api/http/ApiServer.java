package io.realmit.edwige.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.callbacks.ValidateRegistrationCallbackClient;
import io.realmit.edwige.api.controllers.console.ConsoleCommandController;
import io.realmit.edwige.api.controllers.info.InfoController;
import io.realmit.edwige.api.controllers.player.PlayerController;
import io.realmit.edwige.api.controllers.validateRegistration.ValidateRegistrationController;
import io.realmit.edwige.api.http.handlers.ConsoleCommandHandler;
import io.realmit.edwige.api.http.handlers.InfoHandler;
import io.realmit.edwige.api.http.handlers.PlayerHandler;
import io.realmit.edwige.api.http.handlers.ValidateRegistrationHandler;
import io.realmit.edwige.api.services.ConsoleCommandService;
import io.realmit.edwige.api.services.InfoService;
import io.realmit.edwige.api.services.PlayerService;
import io.realmit.edwige.api.services.ValidateRegistrationService;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ApiServer {

    private ExecutorService executor;
    private final Plugin plugin;
    private final int port;
    private final String bearerToken;
    private HttpServer server;

    private ValidateRegistrationCallbackClient validateRegistrationCallbackClient;

    private final ChatQuestionService chatQuestionService;
    private final MessageService messageService;

    public ApiServer(
            ChatQuestionService chatQuestionService,
            MessageService messageService,
            Plugin plugin,
            int port,
            String bearerToken
    ) {
        this.chatQuestionService = chatQuestionService;
        this.messageService = messageService;
        this.plugin = plugin;
        this.port = port;
        this.bearerToken = bearerToken;
    }

    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(port);
        server = HttpServer.create(address, 0);
        String endpointsBase = plugin.getConfig().getString("modules.api.endpoints.base");

        initCallbacks();

        // Enabled from config.yml
        enableServerInfo(endpointsBase);
        enableConsoleCommand(endpointsBase);
        enableValidateRegistration(endpointsBase);
        enablePlayer(endpointsBase);

        executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();

        plugin.getLogger().info("API server started on port " + port);
    }

    private void initCallbacks() {
        validateRegistrationCallbackClient = new ValidateRegistrationCallbackClient(plugin, new ObjectMapper());
    }

    private void enableServerInfo(String endpointsBase) {
        if (!plugin.getConfig().getBoolean("modules.api.endpoints.serverInfo.enabled")) {
            return;
        }

        InfoService service = new InfoService();
        InfoController controller = new InfoController(service);
        InfoHandler handler = new InfoHandler(controller, bearerToken);

        String path = plugin.getConfig().getString("modules.api.endpoints.serverInfo.path");
        server.createContext(endpointsBase + path, handler);
    }

    private void enableConsoleCommand(String endpointsBase) {
        if (!plugin.getConfig().getBoolean("modules.api.endpoints.consoleCommand.enabled")) {
            return;
        }

        ConsoleCommandService service = new ConsoleCommandService();
        ConsoleCommandController controller = new ConsoleCommandController(service);
        ConsoleCommandHandler handler = new ConsoleCommandHandler(controller, bearerToken);

        String path = plugin.getConfig().getString("modules.api.endpoints.consoleCommand.path");
        server.createContext(endpointsBase + path, handler);
    }

    private void enableValidateRegistration(String endpointsBase) {
        if (!plugin.getConfig().getBoolean("modules.api.endpoints.validateRegistration.enabled")) {
            return;
        }

        ValidateRegistrationService service = new ValidateRegistrationService(
                chatQuestionService,
                messageService,
                plugin,
                validateRegistrationCallbackClient
        );
        ValidateRegistrationController controller = new ValidateRegistrationController(service);
        ValidateRegistrationHandler handler = new ValidateRegistrationHandler(controller, bearerToken);

        String path = plugin.getConfig().getString("modules.api.endpoints.validateRegistration.path");
        server.createContext(endpointsBase + path, handler);
    }

    private void enablePlayer(String endpointsBase) {
        if (!plugin.getConfig().getBoolean("modules.api.endpoints.player.enabled")) {
            return;
        }

        PlayerService service = new PlayerService(plugin);
        PlayerController controller = new PlayerController(service);
        PlayerHandler handler = new PlayerHandler(controller, bearerToken);

        String path = plugin.getConfig().getString("modules.api.endpoints.player.path");
        server.createContext(endpointsBase + path, handler);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }

        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }

        plugin.getLogger().info("API server stopped");
    }
}
