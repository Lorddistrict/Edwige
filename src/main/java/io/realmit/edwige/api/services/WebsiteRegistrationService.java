package io.realmit.edwige.api.services;

import io.realmit.edwige.api.callbacks.WebsiteCallbackClient;
import io.realmit.edwige.api.dto.requests.WebsiteRegistrationRequest;
import io.realmit.edwige.api.dto.responses.WebsiteRegistrationResponse;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WebsiteRegistrationService {

    private final ChatQuestionService chatQuestionService;
    private final MessageService messageService;
    private final Plugin plugin;
    private final WebsiteCallbackClient callbackClient;

    public WebsiteRegistrationService(
            ChatQuestionService chatQuestionService,
            MessageService messageService,
            Plugin plugin,
            WebsiteCallbackClient callbackClient
    ) {
        this.chatQuestionService = chatQuestionService;
        this.messageService = messageService;
        this.plugin = plugin;
        this.callbackClient = callbackClient;
    }

    public void handleRequest(WebsiteRegistrationRequest request) {
        if (request == null) {
            return;
        }

        String playerName = request.username();
        if (playerName == null || playerName.isBlank()) {
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            Player target = Bukkit.getPlayerExact(playerName);
            if (target == null) {
                return;
            }

            chatQuestionService.askYesNo(
                    target,
                    "api-website-registration-notify-player",
                    answer -> {
                        messageService.send(
                                target,
                                "api-website-registration-player-answer-" + (answer ? "yes" : "no")
                        );

                        callbackClient.sendResponse(request.callbackUrl(), new WebsiteRegistrationResponse(answer));
                    }
            );
        });
    }
}
