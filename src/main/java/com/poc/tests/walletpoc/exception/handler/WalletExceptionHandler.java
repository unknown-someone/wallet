package com.poc.tests.walletpoc.exception.handler;

import com.poc.tests.walletpoc.dto.ErrorResponse;
import com.poc.tests.security.AuthenticationException;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletExceptionHandler {
    @ExceptionHandler(value = {
            NotFoundException.class, InsufficientFundsException.class, StripeServiceException.class, AuthenticationException.class
    })
    public ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().getSimpleName() + " was thrown"), HttpStatus.I_AM_A_TEAPOT);
    }
}
