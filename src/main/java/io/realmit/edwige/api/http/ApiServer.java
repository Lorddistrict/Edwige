package io.realmit.edwige.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.callbacks.WebsiteCallbackClient;
import io.realmit.edwige.api.controllers.requests.ConsoleCommandController;
import io.realmit.edwige.api.controllers.ServerStatsController;
import io.realmit.edwige.api.controllers.requests.WebsiteRegistrationController;
import io.realmit.edwige.api.http.handlers.ConsoleCommandHandler;
import io.realmit.edwige.api.http.handlers.ServerStatsHandler;
import io.realmit.edwige.api.http.handlers.WebsiteRegistrationHandler;
import io.realmit.edwige.api.services.ConsoleCommandService;
import io.realmit.edwige.api.services.ServerStatsService;
import io.realmit.edwige.api.services.WebsiteRegistrationService;
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
    private HttpServer server;

    private WebsiteCallbackClient websiteCallbackClient;

    private ConsoleCommandController consoleCommandController;
    private ServerStatsController serverInfoController;
    private WebsiteRegistrationController websiteRegistrationController;

    private ConsoleCommandHandler consoleCommandHandler;
    private ServerStatsHandler serverInfoHandler;
    private WebsiteRegistrationHandler websiteRegistrationHandler;

    private ConsoleCommandService consoleCommandService;
    private ChatQuestionService chatQuestionService;
    private MessageService messageService;
    private ServerStatsService serverInfoService;
    private WebsiteRegistrationService websiteRegistrationService;

    public ApiServer(
            ChatQuestionService chatQuestionService,
            MessageService messageService,
            Plugin plugin,
            int port
    ) {
        this.chatQuestionService = chatQuestionService;
        this.messageService = messageService;
        this.plugin = plugin;
        this.port = port;
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
        websiteCallbackClient = new WebsiteCallbackClient(plugin, new ObjectMapper());
    }

    private void initServices() {
        serverInfoService = new ServerStatsService();
        consoleCommandService = new ConsoleCommandService(plugin);
        websiteRegistrationService = new WebsiteRegistrationService(
                chatQuestionService,
                messageService,
                plugin,
                websiteCallbackClient
        );
    }

    private void initControllers() {
        serverInfoController = new ServerStatsController(serverInfoService);
        consoleCommandController = new ConsoleCommandController(consoleCommandService);
        websiteRegistrationController = new WebsiteRegistrationController(websiteRegistrationService);
    }

    private void initHandlers() {
        serverInfoHandler = new ServerStatsHandler(serverInfoController);
        consoleCommandHandler = new ConsoleCommandHandler(consoleCommandController);
        websiteRegistrationHandler = new WebsiteRegistrationHandler(websiteRegistrationController);
    }

    private void initEndpoints() {
        server.createContext("/api/server", serverInfoHandler);
        server.createContext("/api/execute", consoleCommandHandler);
        server.createContext("/api/validate-registration", websiteRegistrationHandler);
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
