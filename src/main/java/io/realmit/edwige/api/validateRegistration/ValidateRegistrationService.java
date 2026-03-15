package io.realmit.edwige.api.validateRegistration;

import io.realmit.edwige.api.services.interfaces.RequestHandlerServiceInterface;
import io.realmit.edwige.services.chat.ChatQuestionService;
import io.realmit.edwige.services.chat.MessageService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ValidateRegistrationService implements RequestHandlerServiceInterface<ValidateRegistrationRequest> {

    private final ChatQuestionService chatQuestionService;
    private final MessageService messageService;
    private final Plugin plugin;
    private final ValidateRegistrationCallbackClient callbackClient;

    public ValidateRegistrationService(
            ChatQuestionService chatQuestionService,
            MessageService messageService,
            Plugin plugin,
            ValidateRegistrationCallbackClient callbackClient
    ) {
        this.chatQuestionService = chatQuestionService;
        this.messageService = messageService;
        this.plugin = plugin;
        this.callbackClient = callbackClient;
    }

    @Override
    public void handleRequest(ValidateRegistrationRequest request) {
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

            String question = messageService.getMessageFromKey("api.website.registration.question");
            String prefix0 = messageService.getMessageFromKey("api.website.registration.prefix.0");
            String prefix1 = messageService.getMessageFromKey("api.website.registration.prefix.1");
            String prefix2 = messageService.getMessageFromKey("api.website.registration.prefix.2");
            String prefix3 = messageService.getMessageFromKey("api.website.registration.prefix.3");
            String prefix4 = messageService.getMessageFromKey("api.website.registration.prefix.4");

            Component prefix = Component.text()
                    .append(Component.text(question, NamedTextColor.WHITE))
                    .append(Component.text("\n\n"))
                    .append(Component.text(prefix0, NamedTextColor.GRAY))
                    .append(Component.text(request.email(), NamedTextColor.WHITE))
                    .append(Component.text("\n"))
                    .append(Component.text(prefix1, NamedTextColor.GRAY))
                    .append(Component.text(request.ip(), NamedTextColor.WHITE))
                    .append(Component.text("\n\n"))
                    .append(Component.text(prefix2, NamedTextColor.WHITE))
                    .append(Component.text("\n"))
                    .append(Component.text(prefix3, NamedTextColor.YELLOW))
                    .append(Component.text(request.timeout(), NamedTextColor.DARK_PURPLE))
                    .append(Component.text(prefix4, NamedTextColor.YELLOW))
                    .append(Component.text("\n"))
                    .build();

            Component suffix = Component.empty();

            chatQuestionService.askYesNo(
                    target,
                    true,
                    "api.website.registration.title",
                    true,
                    prefix,
                    suffix,
                    answer -> {
                        String choice = answer ? "confirm" : "deny";
                        String trans = messageService.getMessageFromKey("api.website.registration.answers." + choice);
                        String check = messageService.getMessageFromKey("symbols.check");
                        String cross = messageService.getMessageFromKey("symbols.cross");
                        String symbol = answer ? check : cross;
                        NamedTextColor color = answer ? NamedTextColor.GREEN : NamedTextColor.RED;
                        target.sendMessage(Component.text()
                                .append(Component.text("\n"))
                                .append(Component.text(symbol, color))
                                .append(Component.text(" "))
                                .append(Component.text(trans, NamedTextColor.WHITE))
                                .append(Component.text("\n"))
                                .build()
                        );

                        callbackClient.sendResponse(request.callbackUrl(), new ValidateRegistrationResponse(answer));
                    }
            );
        });
    }
}
