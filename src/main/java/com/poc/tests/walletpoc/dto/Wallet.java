package com.poc.tests.walletpoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Wallet {
    private Long id;

    private BigDecimal balance;
}
