package io.realmit.edwige.api.shared.dto;

import java.util.List;

public record ValidationErrorResponse(List<String> errors) {}