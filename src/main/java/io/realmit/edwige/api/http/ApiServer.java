package io.realmit.edwige.api.http;

import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.controllers.requests.ConsoleCommandController;
import io.realmit.edwige.api.controllers.ServerStatsController;
import io.realmit.edwige.api.http.handlers.ConsoleCommandHandler;
import io.realmit.edwige.api.http.handlers.ServerStatsHandler;
import io.realmit.edwige.api.services.ConsoleCommandService;
import io.realmit.edwige.api.services.PendingItemStoreService;
import io.realmit.edwige.api.services.ServerStatsService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final public class ApiServer {

    private ExecutorService executor;
    final private Plugin plugin;
    final private int port;
    private HttpServer server;

    private ServerStatsService serverInfoService;
    private ServerStatsController serverInfoController;
    private ServerStatsHandler serverInfoHandler;

    private ConsoleCommandService executeService;
    private ConsoleCommandController executeController;
    private ConsoleCommandHandler executeHandler;

    public ApiServer(
            MessageService messageService,
            PendingItemStoreService pendingItemStoreService,
            Plugin plugin,
            int port
    ) {
        this.plugin = plugin;
        this.port = port;
    }

    public void start() throws IOException {
        InetSocketAddress address = new InetSocketAddress(port);
        server = HttpServer.create(address, 0);

        initServices();
        initControllers();
        initHandlers();
        initEndpoints();

        executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();

        plugin.getLogger().info("API server started on port " + port);
    }

    private void initServices() {
        serverInfoService = new ServerStatsService();
        executeService = new ConsoleCommandService(plugin);
    }

    private void initControllers() {
        serverInfoController = new ServerStatsController(serverInfoService);
        executeController = new ConsoleCommandController(executeService);
    }

    private void initHandlers() {
        serverInfoHandler = new ServerStatsHandler(serverInfoController);
        executeHandler = new ConsoleCommandHandler(executeController);
    }

    private void initEndpoints() {
        server.createContext("/api/server", serverInfoHandler);
        server.createContext("/api/execute", executeHandler);
    }

    public void stop() {
        if (null != server) {
            server.stop(0);
            server = null;
        }

        if (null != executor) {
            executor.shutdownNow();
            executor = null;
        }

        plugin.getLogger().info("API server stopped");
    }
}
