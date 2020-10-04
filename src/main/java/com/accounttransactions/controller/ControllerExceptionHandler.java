package com.accounttransactions.controller;

import com.accounttransactions.dto.ErrorDTO;
import com.accounttransactions.exception.AccountNotFoundException;
import com.accounttransactions.exception.GenericValidateRuntimeException;
import com.accounttransactions.exception.InvalidOperationTypeException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleEntityNotFound(AccountNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.NOT_FOUND, ex), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOperationTypeException.class)
    protected ResponseEntity<ErrorDTO> handleEntityInvalidOperationType(InvalidOperationTypeException ex) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GenericValidateRuntimeException.class)
    protected ResponseEntity<ErrorDTO> handleGenericValidateRuntimeException(GenericValidateRuntimeException ex) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, ex), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBody(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, "Invalid format!"), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorDTO> handleInvalidBody(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage).collect(Collectors.toList());
        String error = String.join(", ", errors);
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, error), HttpStatus.BAD_REQUEST);
    }


}