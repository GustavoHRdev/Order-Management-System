package io.github.gustavohrdev.exception;

public class ProdutoNotFoundException extends NotFoundException {

    public ProdutoNotFoundException() {
        super("Produto inválido!");
    }
}
