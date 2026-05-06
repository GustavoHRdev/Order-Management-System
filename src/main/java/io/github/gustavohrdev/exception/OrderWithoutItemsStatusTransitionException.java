package io.github.gustavohrdev.exception;

public class OrderWithoutItemsStatusTransitionException extends BusinessRuleException {

    public OrderWithoutItemsStatusTransitionException() {
        super("Nao e permitido avancar o status de um pedido sem itens.");
    }
}
