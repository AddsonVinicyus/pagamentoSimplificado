package com.adx.pagamentoSimplificado.infra.exceptions;

public class InsufficientAmountException extends RuntimeException {

    public InsufficientAmountException() { super("Saldo insuficiente.");}
    public InsufficientAmountException(String message) {
        super(message);
    }
}
