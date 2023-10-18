package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUserCaseHandler implements UserSignUpUseCase {

    private final CreateUserPort createUserPort;
    private final ModelMapper appModelMapper;

    @Override
    public User signUp(SignUpCommand command) {
        User newUser = appModelMapper.map(command, User.class);
        return createUserPort.creatUser(newUser);
    }
}
