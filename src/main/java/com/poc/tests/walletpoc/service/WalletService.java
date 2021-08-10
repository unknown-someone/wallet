package com.poc.tests.walletpoc.service;

import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import com.poc.tests.walletpoc.entity.WalletEntity;
import com.poc.tests.walletpoc.exception.InsufficientFundsException;
import com.poc.tests.walletpoc.exception.NotFoundException;
import com.poc.tests.walletpoc.exception.StripeServiceException;
import com.poc.tests.walletpoc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    private final StripeService stripeService;

    public Optional<WalletEntity> findById(Long id) {
        return walletRepository.findById(id);
    }

    public void rechargeById(Long id, Recharge recharge) throws NotFoundException, StripeServiceException {
        WalletEntity walletEntity = walletRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        stripeService.charge(recharge.getCardNumber(), recharge.getAmount());

        BigDecimal newBalance = walletEntity.getBalance()
                .add(recharge.getAmount());

        walletEntity.setBalance(newBalance);
    }

    public void payById(Long id, Payment payment) throws NotFoundException, InsufficientFundsException {
        WalletEntity walletEntity = walletRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        BigDecimal newBalance = walletEntity.getBalance()
                .subtract(payment.getAmount());

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }

        walletEntity.setBalance(newBalance);
    }
}
