package com.poc.tests.walletpoc.service.impl;


import com.poc.tests.walletpoc.exception.StripeServiceException;
import com.poc.tests.walletpoc.service.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class StripeServiceTest {

    StripeService s = new StripeService();

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeServiceException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
