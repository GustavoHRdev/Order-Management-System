package io.github.gustavohrdev.exception;

public class InvalidOrderStatusException extends ValidationException {

    public InvalidOrderStatusException() {
        super("Status inválido.");
    }
}
