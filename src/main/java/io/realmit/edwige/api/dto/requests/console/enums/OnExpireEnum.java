package io.realmit.edwige.api.dto.requests.console.enums;

public enum OnExpireEnum {
    DISCARD("DISCARD"),
    RETRY("RETRY");

    private final String onExpire;

    OnExpireEnum(String onExpire) {
        this.onExpire = onExpire;
    }

    public String onExpire() {
        return onExpire;
    }
}
