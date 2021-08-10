package com.poc.tests.walletpoc.api;

import com.poc.tests.walletpoc.dto.Wallet;
import com.poc.tests.walletpoc.mapper.WalletMapper;
import com.poc.tests.walletpoc.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    @RequestMapping("/wallet/{id}")
    public Wallet findById(@PathVariable("id") Long id) {
        log.info("Logging from /TODO");

        return walletService.findById(id)
                .map(WalletMapper::toDto)
                .orElseThrow(RuntimeException::new);
    }
}
