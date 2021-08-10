package com.poc.tests.walletpoc.repository;

import com.poc.tests.walletpoc.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<WalletEntity> findById(Long id);

    Optional<WalletEntity> findByIdAndPassword(Long id, String password);
}
