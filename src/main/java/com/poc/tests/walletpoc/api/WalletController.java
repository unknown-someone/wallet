package com.poc.tests.walletpoc.api;

import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import com.poc.tests.walletpoc.dto.Wallet;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NegativePaymentException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import com.poc.tests.walletpoc.mapper.WalletMapper;
import com.poc.tests.walletpoc.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    @GetMapping("/wallet")
    public Wallet find() throws NotFoundException {
        log.info("Logging from get wallet");

        return walletService.findById(getUserId())
                .map(WalletMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping("/wallet/recharge")
    public void recharge(@Valid @RequestBody Recharge recharge) throws NotFoundException, StripeServiceException {
        log.info("Logging from recharge wallet");

        walletService.rechargeById(getUserId(), recharge);
    }

    @PostMapping("/wallet/payment")
    public void pay(@RequestBody Payment payment) throws NotFoundException, InsufficientFundsException, NegativePaymentException {
        log.info("Logging from pay with wallet");

        walletService.payById(getUserId(), payment);
    }

    private Long getUserId() {
        return Long.decode(
                (String) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
        );
    }
}
