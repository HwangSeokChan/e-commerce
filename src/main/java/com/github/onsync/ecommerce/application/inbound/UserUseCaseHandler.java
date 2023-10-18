package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UserUseCaseHandler implements UserSignUpUseCase, UserUpdateUseCase {

    private final UpdateUserPort updateUserPort;
    private final ModelMapper appModelMapper;
    private final CreateUserPort createUserPort;

    @Override
    public User signUp(SignUpCommand command) {
        User newUser = appModelMapper.map(command, User.class);
        return createUserPort.creatUser(newUser);
    }

    @Override
    public User updateAll(UpdateCommand command) {
        User newUser = appModelMapper.map(command, User.class);
        return updateUserPort.updateUser(newUser);
    }
}
