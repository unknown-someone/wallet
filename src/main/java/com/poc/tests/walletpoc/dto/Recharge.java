package com.poc.tests.walletpoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recharge {
    @NotBlank
    private String cardNumber;

    @NotNull
    private BigDecimal amount;
}
