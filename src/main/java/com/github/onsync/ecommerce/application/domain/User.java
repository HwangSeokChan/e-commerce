package com.github.onsync.ecommerce.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
public class User {

    UserId userId;
    LoginInfo loginInfo;
    UserInfo userInfo;


    @Value
    public static class LoginInfo {
        String id;
        String password;
    }

    @Value
    public static class UserInfo {
        String name;
        String regNo;
    }

    @Value
    public static class UserId {
        Long value;
    }
}
