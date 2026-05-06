package io.github.gustavohrdev.exception;

public class OrderItemModificationNotAllowedException extends BusinessRuleException {

    public OrderItemModificationNotAllowedException() {
        super("Nao e permitido alterar itens de um pedido que nao esta pendente.");
    }
}
