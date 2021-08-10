package com.poc.tests.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    //Not encrypting this in any way since it is a PoC
    private String password;
}
