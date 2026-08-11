package com.adx.pagamentoSimplificado.infra.exceptions;

public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }
}
