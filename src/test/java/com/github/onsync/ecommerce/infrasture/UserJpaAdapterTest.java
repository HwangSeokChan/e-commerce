package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@SpringBootTest
public class UserJpaAdapterTest {

    @Autowired
    UserJpaAdapter userJpaAdapter;
    @Autowired
    UserJpaRepository userJpaRepository;

    @AfterEach
    void 롤백() {
        userJpaRepository.deleteAll();
    }

    @Test
    void 유저생성_테스트_성공() {
        // given
        final User reqUser = new User(null, new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        // when
        User newUser = userJpaAdapter.creatUser(reqUser);
        // then
        Assertions.assertNotNull(newUser.getUserId());
        Assertions.assertEquals(reqUser.getLoginInfo().getId(), newUser.getLoginInfo().getId());
        Assertions.assertEquals(reqUser.getLoginInfo().getPassword(), newUser.getLoginInfo().getPassword());
        Assertions.assertEquals(reqUser.getUserInfo().getName(), newUser.getUserInfo().getName());
        Assertions.assertEquals(reqUser.getUserInfo().getRegNo(), newUser.getUserInfo().getRegNo());
    }

    @Test
    void 유저생성_테스트_실패_EntityExistsException() {
        // given
        User reqUser = new User(null, new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        User existUser = userJpaAdapter.creatUser(reqUser);
        // when
        Executable duplicatedRequest = () -> userJpaAdapter.creatUser(reqUser);
        // then
        Assertions.assertThrows(EntityExistsException.class, duplicatedRequest);
    }

    @Test
    void 유저수정_테스트_성공() {
        // given
        User reqCraeteUser = new User(null, new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        User createdUser = userJpaAdapter.creatUser(reqCraeteUser);
        User reqUpdateUser = new User(createdUser.getUserId(), new User.LoginInfo("id_ZZZ", "password_ZZZ"), new User.UserInfo("name_ZZZ", "regNo_ZZZ"));
        // when
        User updatedUser = userJpaAdapter.updateUser(reqUpdateUser);
        // then
        Assertions.assertNotNull(updatedUser.getUserId());
        Assertions.assertEquals(reqUpdateUser.getLoginInfo().getId(), updatedUser.getLoginInfo().getId());
        Assertions.assertEquals(reqUpdateUser.getLoginInfo().getPassword(), updatedUser.getLoginInfo().getPassword());
        Assertions.assertEquals(reqUpdateUser.getUserInfo().getName(), updatedUser.getUserInfo().getName());
        Assertions.assertEquals(reqUpdateUser.getUserInfo().getRegNo(), updatedUser.getUserInfo().getRegNo());
    }

    @Test
    void 유저수정_테스트_실패_NoSuchElementException() {
        // given
        User reqUpdateUser = new User(new User.UserId(1L), new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        // when
        Executable noTargetRequest = () -> userJpaAdapter.updateUser(reqUpdateUser);
        // then
        Assertions.assertThrows(NoSuchElementException.class, noTargetRequest);
    }
}
