package io.github.gustavohrdev.exception;

public class OrderStatusRegressionNotAllowedException extends BusinessRuleException {

    public OrderStatusRegressionNotAllowedException() {
        super("Nao e permitido retornar o pedido para pendente.");
    }
}
