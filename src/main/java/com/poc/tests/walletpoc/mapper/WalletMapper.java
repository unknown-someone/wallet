package com.poc.tests.walletpoc.mapper;

import com.poc.tests.walletpoc.dto.Wallet;
import com.poc.tests.walletpoc.entity.WalletEntity;

public class WalletMapper {
    public static Wallet toDto(WalletEntity walletEntity) {
        return new Wallet(walletEntity.getId(), walletEntity.getBalance());
    }
}
