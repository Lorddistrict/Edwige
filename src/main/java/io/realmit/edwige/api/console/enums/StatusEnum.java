package io.realmit.edwige.api.console.enums;

public enum StatusEnum {
    SUCCESS("SUCCESS"),
    QUEUED("QUEUED"),
    FAILED("FAILED");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
}
