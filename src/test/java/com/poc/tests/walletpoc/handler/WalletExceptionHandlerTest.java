package com.poc.tests.walletpoc.handler;

import com.poc.tests.walletpoc.dto.ErrorResponse;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.handler.WalletExceptionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WalletExceptionHandlerTest {
    @Test
    public void testHandler() {
        WalletExceptionHandler walletExceptionHandler = new WalletExceptionHandler();

        ResponseEntity<Object> responseEntity = walletExceptionHandler.handleException(new NotFoundException());

        Assertions.assertEquals(HttpStatus.I_AM_A_TEAPOT, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals("NotFoundException was thrown", ((ErrorResponse) responseEntity.getBody()).getErrorMessage());
    }
}
