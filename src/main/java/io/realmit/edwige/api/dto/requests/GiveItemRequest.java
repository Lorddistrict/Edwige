package io.realmit.edwige.api.dto.requests;

public record GiveItemRequest(
        String player,
        String item,
        int amount
) {}
