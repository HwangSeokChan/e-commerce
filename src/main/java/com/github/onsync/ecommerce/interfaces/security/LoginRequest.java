package com.github.onsync.ecommerce.interfaces.security;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {
    private String loginId;
    private String password;
}
