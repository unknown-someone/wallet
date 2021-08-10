package com.poc.tests.walletpoc.service;

import com.poc.tests.walletpoc.entity.WalletEntity;
import com.poc.tests.walletpoc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Optional<WalletEntity> findById(Long id) {
        return walletRepository.findById(id);
    }
}
