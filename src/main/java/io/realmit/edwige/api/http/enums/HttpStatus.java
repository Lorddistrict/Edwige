package io.realmit.edwige.api.http.enums;

public enum HttpStatus {
    HTTP_OK(200, "Ok"),
    HTTP_CREATED(201, "Created"),
    HTTP_NO_CONTENT(204, "No Content"),
    HTTP_BAD_REQUEST(400, "Bad Request"),
    HTTP_UNAUTHORIZED(401, "Unauthorized"),
    HTTP_FORBIDDEN(403, "Forbidden"),
    HTTP_NOT_FOUND(404, "Not Found"),
    HTTP_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    HTTP_INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String reason;

    HttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int code() {
        return code;
    }

    public String reason() {
        return reason;
    }
}
