package com.poc.tests.walletpoc.service;

import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import com.poc.tests.walletpoc.entity.WalletEntity;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NegativePaymentException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import com.poc.tests.walletpoc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {
    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    private final StripeService stripeService;

    public Optional<WalletEntity> findById(Long id) {
        log.info("Starting get wallet by id");

        return walletRepository.findById(id);
    }

    public Optional<WalletEntity> findByIdAndPassword(Long id, String password) {
        log.info("Starting get wallet by id and password");

        return walletRepository.findByIdAndPassword(id, password);
    }

    public void rechargeById(Long id, Recharge recharge) throws NotFoundException, StripeServiceException {
        log.info("Starting recharge wallet");

        WalletEntity walletEntity = walletRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        log.info("Wallet entity locked for recharge");

        // if this was not a dummy service, there could be an issue
        // if charge succeeds but the rest of the request fails
        // and retry or refund would be needed - ignored for simplicity
        stripeService.charge(recharge.getCardNumber(), recharge.getAmount());

        BigDecimal newBalance = walletEntity.getBalance()
                .add(recharge.getAmount());

        walletEntity.setBalance(newBalance);
    }

    public void payById(Long id, Payment payment) throws NotFoundException, InsufficientFundsException, NegativePaymentException {
        log.info("Starting pay with wallet");

        WalletEntity walletEntity = walletRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        log.info("Wallet entity locked for payment");

        if (payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePaymentException();
        }

        BigDecimal newBalance = walletEntity.getBalance()
                .subtract(payment.getAmount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }

        walletEntity.setBalance(newBalance);
    }
}
