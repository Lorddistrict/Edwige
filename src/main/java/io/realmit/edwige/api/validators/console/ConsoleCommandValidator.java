package io.realmit.edwige.api.validators.console;

import io.realmit.edwige.api.dto.requests.console.CommandRequest;
import io.realmit.edwige.api.dto.requests.console.ConsoleCommandRequest;
import io.realmit.edwige.api.dto.requests.console.enums.OnExpireEnum;
import io.realmit.edwige.api.validators.AbstractValidator;

public class ConsoleCommandValidator extends AbstractValidator<ConsoleCommandRequest> {

    @Override
    protected void validate(ConsoleCommandRequest request) {

        if (request.commands() == null || request.commands().isEmpty()) {
            reject("Commands list cannot be null or empty");
            return;
        }

        for (int i = 0; i < request.commands().size(); i++) {
            CommandRequest commandRequest = request.commands().get(i);

            if (commandRequest.command() == null || commandRequest.command().isBlank()) {
                reject("Command at index " + i + " cannot be null or blank");
                return;
            }

            if (Boolean.TRUE.equals(commandRequest.waitForPlayer()) && commandRequest.targetPlayer() == null) {
                reject("targetPlayer is required when waitForPlayer is true at index " + i);
                return;
            }
        }

        if (request.onExpire() != null) {
            try {
                OnExpireEnum.valueOf(request.onExpire().name());
            } catch (IllegalArgumentException e) {
                reject("Invalid onExpire value: " + request.onExpire());
                return;
            }
        }

        if (request.ttlSeconds() != null && request.ttlSeconds() <= 0) {
            reject("ttlSeconds must be positive");
        }
    }
}
