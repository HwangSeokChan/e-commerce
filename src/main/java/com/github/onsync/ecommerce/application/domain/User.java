package com.github.onsync.ecommerce.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
public class User {

    UserId userId;
    LoginInfo loginInfo;
    UserInfo userInfo;
    boolean usable;

    public User(UserId userId, LoginInfo loginInfo, UserInfo userInfo) {
        this.userId = userId;
        this.loginInfo = loginInfo;
        this.userInfo = userInfo;
        this.usable = true;
    }

    public void resign() {
        usable = false;
    }

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
