package io.github.gustavohrdev.exception;

public class InvalidEmailException extends ValidationException {

    public InvalidEmailException() {
        super("Email inválido.");
    }
}
