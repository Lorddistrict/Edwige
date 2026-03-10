package io.realmit.edwige.api.dto.requests.console.enums;

public enum OnExpire {
    DISCARD("DISCARD"),
    RETRY("RETRY");

    private final String onExpire;

    OnExpire(String onExpire) {
        this.onExpire = onExpire;
    }

    public String onExpire() {
        return onExpire;
    }
}
