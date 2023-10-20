package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.LoadUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUseCaseHandler implements UserSignUpUseCase, UserUpdateUseCase, UserResignUseCase, UserDetailsService {

    private final ModelMapper appModelMapper;
    private final UpdateUserPort updateUserPort;
    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User signUp(SignUpCommand command) {
        User newUser = appModelMapper.map(command, User.class);

        loadUserPort.findByLoginId(newUser.getLoginInfo().getId())
                .orElseThrow(EntityExistsException::new);

        return createUserPort.creatUser(newUser);
    }

    @Override
    public User updateAll(UpdateCommand command) {
        User updateTargetUser = appModelMapper.map(command, User.class);

        loadUserPort.findByUserId(updateTargetUser.getUserId())
                .orElseThrow(NoSuchElementException::new);

        return updateUserPort.updateUser(updateTargetUser);
    }

    @Override
    public User resign(ResignCommand command) {
        User resignTargetUser = parseToUser(command);

        String rawPassword = resignTargetUser.getLoginInfo().getPassword();

        loadUserPort.findByLoginId(resignTargetUser.getLoginInfo().getId())
                .map(user -> passwordEncoder.matches(rawPassword, user.getLoginInfo().getPassword()))
                .orElseThrow(NoSuchElementException::new);

        resignTargetUser.resign();

        return updateUserPort.updateUser(resignTargetUser);
    }

    private User parseToUser(UserCommand command) {
        return appModelMapper.map(command, User.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return loadUserPort.findByLoginId(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getLoginInfo().getId(),
                        user.getLoginInfo().getPassword(),
                        new ArrayList<>()))
                .orElseThrow(NoSuchElementException::new);
    }
}