package io.github.gustavohrdev.exception;

public class PedidoNotFoundException extends NotFoundException {

    public PedidoNotFoundException() {
        super("Pedido inválido!");
    }
}
