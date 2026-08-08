package com.adx.pagamentoSimplificado.infra;

import com.adx.pagamentoSimplificado.dto.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> threatDuplicateEntry(DataIntegrityViolationException exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO("Usuário já cadastrado", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> threatNotFound(EntityNotFoundException exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO("NOT FOUND", HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity threatRuntimeException(RuntimeException exception){
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }

}
