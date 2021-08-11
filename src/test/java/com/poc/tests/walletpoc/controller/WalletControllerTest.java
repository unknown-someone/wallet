package com.poc.tests.walletpoc.controller;

import com.poc.tests.security.AuthenticationException;
import com.poc.tests.security.TokenController;
import com.poc.tests.security.User;
import com.poc.tests.walletpoc.dto.Payment;
import com.poc.tests.walletpoc.dto.Recharge;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    private static final Long ID = 1L;
    private static final String PASSWORD = "pass";

    @Autowired
    private TokenController tokenController;

    @Test
    public void testFind() throws AuthenticationException {
        RestAssured.given()
                .headers(getHeaders())
                .contentType("application/json")
                .when()
                .get("/wallet")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1));
//                .body("balance", Matchers.is(Matchers.closeTo(100, 0.01)));
    }

    @Test
    public void testRecharge() throws AuthenticationException {
        RestAssured.given()
                .headers(getHeaders())
                .contentType("application/json")
                .body(new Recharge("1234", BigDecimal.TEN))
                .when()
                .post("/wallet/recharge")
                .then()
                .statusCode(200);
    }

    @Test
    public void testPay() throws AuthenticationException {
        RestAssured.given()
                .headers(getHeaders())
                .contentType("application/json")
                .body(new Payment(BigDecimal.TEN))
                .when()
                .post("/wallet/payment")
                .then()
                .statusCode(200);
    }

    private Headers getHeaders() throws AuthenticationException {
        return new Headers(
                new Header("Authorization", tokenController.generateToken(new User(ID, PASSWORD)).getToken())
        );
    }
}
