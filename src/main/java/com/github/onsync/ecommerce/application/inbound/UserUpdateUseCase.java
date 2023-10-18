package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import lombok.Value;

public interface UserUpdateUseCase {

    // TODO : use put method
    User updateAll(UpdateCommand command);

    @Value
    class UpdateCommand {
        User.UserId userId;
        User.LoginInfo loginInfo;
        User.UserInfo userInfo;
    }
}
