package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.LoadUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements CreateUserPort, UpdateUserPort, LoadUserPort {

    private final ModelMapper infraModelMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public User creatUser(User user) {

        UserData reqUserData = infraModelMapper.map(user, UserData.class);

        UserData newUserData = userJpaRepository.save(reqUserData);

        return infraModelMapper.map(newUserData, User.class);
    }

    @Override
    public User updateUser(User user) {
        UserData reqUserData = infraModelMapper.map(user, UserData.class);
        UserData updatedUserData = userJpaRepository.save(reqUserData);
        return infraModelMapper.map(updatedUserData, User.class);
    }

    @Override
    public Optional<User> findByUserId(User.UserId userId) {
        return userJpaRepository.findById(userId.getValue())
                .map(data -> infraModelMapper.map(data, User.class));
    }

    @Override
    public Optional<User> findByLoginInfo(User.LoginInfo loginInfo) {
        return userJpaRepository.findByLoginId(loginInfo.getId())
                .map(data -> infraModelMapper.map(data, User.class));
    }
}
