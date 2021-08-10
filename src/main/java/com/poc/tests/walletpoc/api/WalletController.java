package com.poc.tests.walletpoc.api;

import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import com.poc.tests.walletpoc.dto.Wallet;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import com.poc.tests.walletpoc.mapper.WalletMapper;
import com.poc.tests.walletpoc.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    @GetMapping("/wallet/{id}")
    public Wallet findById(@PathVariable("id") Long id) throws NotFoundException {
        log.info("Logging from get wallet");

        return walletService.findById(id)
                .map(WalletMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping("/wallet/{id}/recharge")
    public void rechargeById(@PathVariable("id") Long id, @Valid @RequestBody Recharge recharge) throws NotFoundException, StripeServiceException {
        log.info("Logging from recharge wallet");

        walletService.rechargeById(id, recharge);
    }

    @PostMapping("/wallet/{id}/payment")
    public void payById(@PathVariable("id") Long id, @RequestBody Payment payment) throws NotFoundException, InsufficientFundsException {
        log.info("Logging from pay with wallet");

        walletService.payById(id, payment);
    }
}
