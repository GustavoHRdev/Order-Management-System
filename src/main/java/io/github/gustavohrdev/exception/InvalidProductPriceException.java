package io.github.gustavohrdev.exception;

public class InvalidProductPriceException extends ValidationException {

    public InvalidProductPriceException() {
        super("Preço deve ser maior que zero.");
    }
}
