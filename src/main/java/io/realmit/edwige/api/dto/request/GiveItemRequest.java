package io.realmit.edwige.api.dto.request;

public record GiveItemRequest(
        String player,
        String item,
        int amount
) {}
