package io.github.gustavohrdev.api.dto.response;

public record ApiErrorResponse(String timestamp, int status, String error, String message, String path) {
}

