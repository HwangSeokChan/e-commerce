package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import lombok.Value;

public interface UserResignUseCase {

    User resign(ResignCommand command);

    @Value
    class ResignCommand implements UserCommand {
        User.UserId userId;
        User.LoginInfo loginInfo;
    }
}
