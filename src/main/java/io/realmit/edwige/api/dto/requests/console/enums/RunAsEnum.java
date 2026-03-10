package io.realmit.edwige.api.dto.requests.console.enums;

public enum RunAsEnum {
    CONSOLE("CONSOLE"),
    PLAYER("PLAYER");

    private final String runAs;

     RunAsEnum(String runAs) {
        this.runAs = runAs;
     }

     public String runAs() {
         return runAs;
     }
}
