package io.github.gustavohrdev.exception;

public class InvalidProductNameException extends ValidationException {

    public InvalidProductNameException() {
        super("Nome do produto inválido.");
    }
}
