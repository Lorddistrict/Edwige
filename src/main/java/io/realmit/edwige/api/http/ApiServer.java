package io.realmit.edwige.api.http;

import com.sun.net.httpserver.HttpServer;
import io.realmit.edwige.api.http.handlers.GiveItemHandler;
import io.realmit.edwige.api.controller.GiveItemController;
import io.realmit.edwige.api.service.GiveItemService;
import io.realmit.edwige.api.service.PendingItemStoreService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ApiServer {

    private ExecutorService executor;
    private final MessageService messageService;
    private final PendingItemStoreService pendingItemStoreService;
    private final Plugin plugin;
    private final int port;
    private HttpServer server;

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

        initEndpoints();

        executor = Executors.newCachedThreadPool();
        server.setExecutor(executor);
        server.start();

        plugin.getLogger().info("API server started on port " + port);
    }

    public void initEndpoints() {
        GiveItemService giveItemService = new GiveItemService(messageService, pendingItemStoreService, plugin);
        GiveItemController giveItemController = new GiveItemController(giveItemService);
        GiveItemHandler giveItemHandler = new GiveItemHandler(giveItemController);
        server.createContext("/api/give", giveItemHandler);
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
