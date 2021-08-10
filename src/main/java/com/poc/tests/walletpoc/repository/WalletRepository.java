package com.poc.tests.walletpoc.repository;

import com.poc.tests.walletpoc.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
