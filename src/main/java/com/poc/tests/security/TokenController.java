package com.poc.tests.security;

import com.poc.tests.walletpoc.entity.WalletEntity;
import com.poc.tests.walletpoc.service.WalletService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private Logger log = LoggerFactory.getLogger(TokenController.class);

    private final WalletService walletService;

    @PostMapping("/token")
    public Token generateToken(@Valid @RequestBody User user) throws AuthenticationException {
        log.info("Logging from generate token");

        WalletEntity walletEntity = walletService.findByIdAndPassword(user.getId(), user.getPassword())
                .orElseThrow(AuthenticationException::new);

        return getJWTToken(user.getId());
    }

    private Token getJWTToken(Long id) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("USER");

        String token = Jwts
                .builder()
                .setId("thisIsProbablyImportant")
                .setSubject(id.toString())
                .claim(
                        "authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return new Token(id, "Bearer " + token);
    }
}
