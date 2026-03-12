package io.realmit.edwige.api.dto.responses.console.enums;

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
