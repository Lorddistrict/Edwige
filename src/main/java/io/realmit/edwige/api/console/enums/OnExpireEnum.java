package io.realmit.edwige.api.console.enums;

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
