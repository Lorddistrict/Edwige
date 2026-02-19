package io.realmit.edwige.api.http.enums;

public enum HttpMethods {
    HTTP_GET("GET"),
    HTTP_POST("POST"),
    HTTP_PUT("PUT"),
    HTTP_PATCH("PATCH"),
    HTTP_DELETE("DELETE");

    private final String method;

    HttpMethods(String method) {
        this.method = method;
    }

    public String method() {
        return method;
    }
}
