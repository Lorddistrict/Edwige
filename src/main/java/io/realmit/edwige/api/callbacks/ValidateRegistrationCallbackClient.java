package io.realmit.edwige.api.callbacks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.realmit.edwige.api.dto.requests.interfaces.ResponseInterface;
import io.realmit.edwige.api.http.enums.HttpMethods;
import org.bukkit.plugin.Plugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static io.realmit.edwige.api.http.utils.JsonUtils.HEADER_KEY_CONTENT_TYPE;
import static io.realmit.edwige.api.http.utils.JsonUtils.HEADER_VALUE_JSON;

public final class ValidateRegistrationCallbackClient {

    private final Plugin plugin;
    private final ObjectMapper objectMapper;

    public ValidateRegistrationCallbackClient(
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

                connection.setRequestMethod(HttpMethods.HTTP_POST.method());
                connection.setDoOutput(true);
                connection.setRequestProperty(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_JSON);

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
