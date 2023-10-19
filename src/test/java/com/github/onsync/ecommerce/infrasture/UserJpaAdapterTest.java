package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void 유저생성_테스트() {
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
    void 유저수정_테스트() {
        // given
        User reqCraeteUser = new User(null, new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        User createdUser = userJpaAdapter.creatUser(reqCraeteUser);
        User reqUpdateUser = new User(createdUser.getUserId(), new User.LoginInfo("id_ZZZ", "password_ZZZ"), new User.UserInfo("name_ZZZ", "regNo_ZZZ"));
        // when
        User updatedUser = userJpaAdapter.updateUser(reqUpdateUser);
        // then
        Assertions.assertEquals(createdUser.getUserId(), updatedUser.getUserId());
        Assertions.assertEquals(reqUpdateUser.getLoginInfo().getId(), updatedUser.getLoginInfo().getId());
        Assertions.assertEquals(reqUpdateUser.getLoginInfo().getPassword(), updatedUser.getLoginInfo().getPassword());
        Assertions.assertEquals(reqUpdateUser.getUserInfo().getName(), updatedUser.getUserInfo().getName());
        Assertions.assertEquals(reqUpdateUser.getUserInfo().getRegNo(), updatedUser.getUserInfo().getRegNo());
    }

    @Test
    void 유저조회_테스트() {
        // given
        User reqCraeteUser = new User(null, new User.LoginInfo("id", "password"), new User.UserInfo("name", "regNo"));
        User createdUser = userJpaAdapter.creatUser(reqCraeteUser);
        // when
        User foundByLoginInfo = userJpaAdapter.findByLoginInfo(createdUser.getLoginInfo()).get();
        // then
        Assertions.assertEquals(createdUser.getUserId(), foundByLoginInfo.getUserId());
        Assertions.assertEquals(createdUser.getLoginInfo().getId(), foundByLoginInfo.getLoginInfo().getId());
        Assertions.assertEquals(createdUser.getLoginInfo().getPassword(), foundByLoginInfo.getLoginInfo().getPassword());
        Assertions.assertEquals(createdUser.getUserInfo().getName(), foundByLoginInfo.getUserInfo().getName());
        Assertions.assertEquals(createdUser.getUserInfo().getRegNo(), foundByLoginInfo.getUserInfo().getRegNo());
    }
}
