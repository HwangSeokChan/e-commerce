package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements CreateUserPort {

    private final UserJpaRepository userJpaRepository;
    private final ModelMapper infraModelMapper;

    @Override
    public User creatUser(User user) {

        UserData reqUserData = infraModelMapper.map(user, UserData.class);

        userJpaRepository.findByLoginId(reqUserData.getLoginId()).ifPresent(data -> {
            throw new EntityExistsException();
        });

        UserData newUserData = userJpaRepository.save(reqUserData);

        return infraModelMapper.map(newUserData, User.class);
    }
}
