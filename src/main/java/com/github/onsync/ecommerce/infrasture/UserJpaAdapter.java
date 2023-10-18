package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements CreateUserPort, UpdateUserPort {

    private final ModelMapper infraModelMapper;
    private final UserJpaRepository userJpaRepository;

    @Override
    public User creatUser(User user) {

        UserData reqUserData = infraModelMapper.map(user, UserData.class);

        userJpaRepository.findByLoginId(reqUserData.getLoginId()).ifPresent(data -> {
            throw new EntityExistsException();
        });

        UserData newUserData = userJpaRepository.save(reqUserData);

        return infraModelMapper.map(newUserData, User.class);
    }

    @Override
    public User update(User user) {

        UserData reqUserData = infraModelMapper.map(user, UserData.class);

        boolean hasNoSuchUser = userJpaRepository.findById(reqUserData.getId()).isEmpty();
        if (hasNoSuchUser) {
            throw new NoSuchElementException(); // TODO : for bad request handling
        }

        UserData updatedUserData = userJpaRepository.save(reqUserData);

        return infraModelMapper.map(updatedUserData, User.class);
    }
}
