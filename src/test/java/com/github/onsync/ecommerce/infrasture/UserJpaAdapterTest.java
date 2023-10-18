package com.github.onsync.ecommerce.infrasture;

import com.github.onsync.ecommerce.application.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserJpaAdapterTest {

    @Autowired
    UserJpaAdapter userJpaAdapter;

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
}
