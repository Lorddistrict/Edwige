package io.realmit.edwige.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.callbacks.ValidateRegistrationCallbackClient;
import io.realmit.edwige.api.controllers.ConsoleCommandController;
import io.realmit.edwige.api.controllers.InfoController;
import io.realmit.edwige.api.controllers.ValidateRegistrationController;
import io.realmit.edwige.api.http.handlers.ConsoleCommandHandler;
import io.realmit.edwige.api.http.handlers.InfoHandler;
import io.realmit.edwige.api.http.handlers.ValidateRegistrationHandler;
import io.realmit.edwige.api.services.ConsoleCommandService;
import io.realmit.edwige.api.services.InfoService;
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

    private ConsoleCommandController consoleCommandController;
    private InfoController serverInfoController;
    private ValidateRegistrationController validateRegistrationController;

    private ConsoleCommandHandler consoleCommandHandler;
    private InfoHandler serverInfoHandler;
    private ValidateRegistrationHandler validateRegistrationHandler;

    private ConsoleCommandService consoleCommandService;
    private final ChatQuestionService chatQuestionService;
    private final MessageService messageService;
    private InfoService serverInfoService;
    private ValidateRegistrationService validateRegistrationService;

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

        initCallbacks();
        initServices();
        initControllers();
        initHandlers();
        initEndpoints();

        executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();

        plugin.getLogger().info("API server started on port " + port);
    }

    private void initCallbacks() {
        validateRegistrationCallbackClient = new ValidateRegistrationCallbackClient(plugin, new ObjectMapper());
    }

    private void initServices() {
        serverInfoService = new InfoService();
        consoleCommandService = new ConsoleCommandService(plugin);
        validateRegistrationService = new ValidateRegistrationService(
                chatQuestionService,
                messageService,
                plugin,
                validateRegistrationCallbackClient
        );
    }

    private void initControllers() {
        serverInfoController = new InfoController(serverInfoService);
        consoleCommandController = new ConsoleCommandController(consoleCommandService);
        validateRegistrationController = new ValidateRegistrationController(validateRegistrationService);
    }

    private void initHandlers() {
        serverInfoHandler = new InfoHandler(serverInfoController, bearerToken);
        consoleCommandHandler = new ConsoleCommandHandler(consoleCommandController, bearerToken);
        validateRegistrationHandler = new ValidateRegistrationHandler(validateRegistrationController, bearerToken);
    }

    private void initEndpoints() {
        server.createContext("/api/info", serverInfoHandler);
        server.createContext("/api/execute", consoleCommandHandler);
        server.createContext("/api/validate-registration", validateRegistrationHandler);
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
