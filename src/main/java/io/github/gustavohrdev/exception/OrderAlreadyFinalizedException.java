package io.github.gustavohrdev.exception;

public class OrderAlreadyFinalizedException extends BusinessRuleException {

    public OrderAlreadyFinalizedException() {
        super("Nao e permitido alterar um pedido finalizado.");
    }
}
