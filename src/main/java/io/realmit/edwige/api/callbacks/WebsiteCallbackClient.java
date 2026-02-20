package io.realmit.edwige.api.callbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmit.edwige.api.dto.requests.interfaces.ResponseInterface;
import org.bukkit.plugin.Plugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class WebsiteCallbackClient {

    private final Plugin plugin;
    private final ObjectMapper objectMapper;

    public WebsiteCallbackClient(
            Plugin plugin,
            ObjectMapper objectMapper
    ) {
        this.plugin = plugin;
        this.objectMapper = objectMapper;
    }

    public <R extends ResponseInterface<R>> void sendResponse(String callbackUrl, R response) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URI uri = new URI(callbackUrl);
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                String json = objectMapper.writeValueAsString(response);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(json.getBytes(StandardCharsets.UTF_8));
                }

                int status = connection.getResponseCode();
                plugin.getLogger().info("[Registration] POST " + callbackUrl + " -> " + status);
                connection.disconnect();
            } catch (Exception e) {
                plugin.getLogger().severe("[Registration] callback failed: " + e.getMessage());
            }
        });
    }
}
