package io.github.gustavohrdev.exception;

public abstract class ValidationException extends RuntimeException {

    protected ValidationException(String message) {
        super(message);
    }
}
