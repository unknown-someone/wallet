package com.poc.tests.walletpoc.service;

import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import com.poc.tests.walletpoc.entity.WalletEntity;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NegativePaymentException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@RequiredArgsConstructor
public class WalletServiceTest {
    private static final Long ID = 1L;
    private static final Long BAD_ID = 1234L;
    private static final String PASSWORD = "pass";
    private static final String BAD_PASSWORD = "bad pass";

    @MockBean
    private StripeService stripeService;

    @Autowired
    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    public void init() throws StripeServiceException {
        Mockito.doNothing()
                .when(stripeService)
                .charge("1234", BigDecimal.TEN);

        Mockito.doThrow(new StripeServiceException())
                .when(stripeService)
                .charge("1234", BigDecimal.ONE);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        Optional<WalletEntity> walletEntity = walletService.findById(ID);

        Assertions.assertTrue(walletEntity.isPresent());
        Assertions.assertEquals(
                BigDecimal.valueOf(100)
                        .setScale(2, RoundingMode.HALF_UP),
                walletEntity.get()
                        .getBalance()
                        .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<WalletEntity> walletEntity = walletService.findById(BAD_ID);

        Assertions.assertFalse(walletEntity.isPresent());
    }

    @Test
    public void testFindByIdAndPassword() {
        Optional<WalletEntity> walletEntity = walletService.findByIdAndPassword(ID, PASSWORD);

        Assertions.assertTrue(walletEntity.isPresent());
        Assertions.assertEquals(
                BigDecimal.valueOf(100)
                        .setScale(2, RoundingMode.HALF_UP),
                walletEntity.get()
                        .getBalance()
                        .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Test
    public void testFindByIdAndPasswordNotFound() {
        Optional<WalletEntity> walletEntity = walletService.findByIdAndPassword(ID, BAD_PASSWORD);

        Assertions.assertFalse(walletEntity.isPresent());
    }

    @Test
    @DirtiesContext
    public void testRechargeById() throws NotFoundException, StripeServiceException {
        walletService.rechargeById(ID, new Recharge("1234", BigDecimal.TEN));

        Optional<WalletEntity> walletEntity = walletService.findById(ID);

        Assertions.assertTrue(walletEntity.isPresent());
        Assertions.assertEquals(
                BigDecimal.valueOf(110)
                        .setScale(2, RoundingMode.HALF_UP),
                walletEntity.get()
                        .getBalance()
                        .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Test
    public void testRechargeByIdStripeException() {
        Assertions.assertThrows(
                StripeServiceException.class,
                () -> walletService.rechargeById(ID, new Recharge("1234", BigDecimal.ONE))
        );
    }

    @Test
    public void testRechargeByIdNotFound() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> walletService.rechargeById(BAD_ID, new Recharge("1234", BigDecimal.TEN))
        );
    }

    @Test
    @DirtiesContext
    public void testPayById() throws NotFoundException, NegativePaymentException, InsufficientFundsException {
        walletService.payById(ID, new Payment(BigDecimal.TEN));

        Optional<WalletEntity> walletEntity = walletService.findById(ID);

        Assertions.assertTrue(walletEntity.isPresent());
        Assertions.assertEquals(
                BigDecimal.valueOf(90)
                        .setScale(2, RoundingMode.HALF_UP),
                walletEntity.get()
                        .getBalance()
                        .setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Test
    public void testPayByIdNegativePaymentException() {
        Assertions.assertThrows(
                NegativePaymentException.class,
                () -> walletService.payById(ID, new Payment(BigDecimal.valueOf(-5L)))
        );
    }

    @Test
    public void testPayByIdInsufficientFundsException() {
        Assertions.assertThrows(
                InsufficientFundsException.class,
                () -> walletService.payById(ID, new Payment(BigDecimal.valueOf(200L)))
        );
    }

    @Test
    public void testPayByIdNotFound() {
        Assertions.assertThrows(
                NotFoundException.class,
                () -> walletService.payById(BAD_ID, new Payment(BigDecimal.TEN))
        );
    }

    //this tests pessimistic locking (default lock acquiring timeout is long enough so both requests will succeed)
    @Test
    public void testConcurrentRechargeAndPayment() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> recharge = CompletableFuture.runAsync(() -> {
            try {
                walletService.rechargeById(ID, new Recharge("asdf", BigDecimal.TEN));
            } catch (NotFoundException | StripeServiceException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> payment = CompletableFuture.runAsync(() -> {
            try {
                walletService.payById(ID, new Payment(BigDecimal.TEN));
            } catch (NotFoundException | InsufficientFundsException | NegativePaymentException e) {
                e.printStackTrace();
            }
        });

        recharge.get();
        payment.get();

        Assertions.assertEquals(
                BigDecimal.valueOf(100)
                        .setScale(2, RoundingMode.HALF_UP),
                walletService.findById(1L)
                        .orElseThrow(RuntimeException::new)
                        .getBalance()
                        .setScale(2, RoundingMode.HALF_UP)
        );
    }
}
