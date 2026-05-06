package io.github.gustavohrdev.exception;

public class InvalidOrderQuantityException extends ValidationException {

    public InvalidOrderQuantityException() {
        super("Quantidade inválida.");
    }
}
