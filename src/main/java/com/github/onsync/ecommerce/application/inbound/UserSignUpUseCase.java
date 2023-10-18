package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import lombok.Value;

public interface UserSignUpUseCase {

    User signUp(UserSignUpUseCase.SignUpCommand command);

    @Value
    class SignUpCommand {
        User.LoginInfo loginInfo;
        User.UserInfo userInfo;
    }
}
