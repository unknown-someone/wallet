package com.poc.tests.walletpoc.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet")
@Data
public class WalletEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    // of course, this shouldn't be here in a real application :D
    private String password;
}
