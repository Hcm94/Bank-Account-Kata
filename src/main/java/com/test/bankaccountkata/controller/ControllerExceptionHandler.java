package com.test.bankaccountkata.controller;

import com.test.bankaccountkata.exception.NoSuchAccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchAccountException.class)
    public ResponseEntity<Object> handleNoSuchAccountException(final NoSuchAccountException exception) {
        String bodyOfResponse = "No account is found for Id";
        return ResponseEntity.badRequest().body(bodyOfResponse + exception.getMessage());
    }
}
