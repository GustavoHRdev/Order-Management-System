package io.github.gustavohrdev.exception;

public class InvalidClientNameException extends ValidationException {

    public InvalidClientNameException() {
        super("Nome inválido.");
    }
}
