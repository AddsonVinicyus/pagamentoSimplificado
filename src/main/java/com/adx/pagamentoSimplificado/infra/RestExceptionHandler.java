package com.adx.pagamentoSimplificado.infra;

import com.adx.pagamentoSimplificado.dto.ExceptionDTO;
import com.adx.pagamentoSimplificado.infra.exceptions.InsufficientAmountException;
import com.adx.pagamentoSimplificado.infra.exceptions.InvalidTransactionException;
import com.adx.pagamentoSimplificado.infra.exceptions.UserAlreadyExistsException;
import com.adx.pagamentoSimplificado.infra.exceptions.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InsufficientAmountException.class)
    private ResponseEntity<ExceptionDTO> insufficientAmountHandler(InsufficientAmountException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, exception.getMessage())
        );
    }

    @ExceptionHandler(InvalidTransactionException.class)
    private ResponseEntity<ExceptionDTO> invalidTransactionHandler(InvalidTransactionException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, exception.getMessage())
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    private ResponseEntity<ExceptionDTO> userAlreadyExistsHandler(UserAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDTO(HttpStatus.CONFLICT, exception.getMessage())
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ExceptionDTO> userNotFoundHandler(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDTO(HttpStatus.NOT_FOUND, exception.getMessage())
        );
    }

}
