package io.realmit.edwige.api.http;

import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.controllers.ServerStatsController;
import io.realmit.edwige.api.http.handlers.GiveItemHandler;
import io.realmit.edwige.api.controllers.GiveItemController;
import io.realmit.edwige.api.http.handlers.ServerStatsHandler;
import io.realmit.edwige.api.services.GiveItemService;
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
    final private MessageService messageService;
    final private PendingItemStoreService pendingItemStoreService;
    final private Plugin plugin;
    final private int port;
    private HttpServer server;

    private GiveItemService giveItemService;
    private GiveItemController giveItemController;
    private GiveItemHandler giveItemHandler;

    private ServerStatsService serverInfoService;
    private ServerStatsController serverInfoController;
    private ServerStatsHandler serverInfoHandler;

    public ApiServer(
            MessageService messageService,
            PendingItemStoreService pendingItemStoreService,
            Plugin plugin,
            int port
    ) {
        this.messageService = messageService;
        this.pendingItemStoreService = pendingItemStoreService;
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
        giveItemService = new GiveItemService(messageService, pendingItemStoreService, plugin);
        serverInfoService = new ServerStatsService();
    }

    private void initControllers() {
        giveItemController = new GiveItemController(giveItemService);
        serverInfoController = new ServerStatsController(serverInfoService);
    }

    private void initHandlers() {
        giveItemHandler = new GiveItemHandler(giveItemController);
        serverInfoHandler = new ServerStatsHandler(serverInfoController);
    }

    private void initEndpoints() {
        server.createContext("/api/give", giveItemHandler);
        server.createContext("/api/server", serverInfoHandler);
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
