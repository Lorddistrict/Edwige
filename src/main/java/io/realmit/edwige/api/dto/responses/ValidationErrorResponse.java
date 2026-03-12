package io.realmit.edwige.api.dto.responses;

import java.util.List;

public record ValidationErrorResponse(List<String> errors) {}