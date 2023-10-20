package com.github.onsync.ecommerce.application.outbound;

import com.github.onsync.ecommerce.application.domain.User;

import java.util.Optional;

public interface LoadUserPort {

    Optional<User> findByUserId(User.UserId userId);
    Optional<User> findByLoginId(String loginId);
}
