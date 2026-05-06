package io.github.gustavohrdev.exception;

public class InvalidOrderStatusTransitionException extends BusinessRuleException {

    public InvalidOrderStatusTransitionException() {
        super("Transicao de status invalida.");
    }
}
