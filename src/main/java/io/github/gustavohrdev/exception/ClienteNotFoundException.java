package io.github.gustavohrdev.exception;

public class ClienteNotFoundException extends NotFoundException {

    public ClienteNotFoundException() {
        super("Cliente inválido!");
    }
}
