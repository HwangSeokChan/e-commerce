package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import org.apache.tomcat.util.buf.HexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SpringBootTest
public class UserUseCaseHandlerTest {

    @Autowired
    UserUseCaseHandler useCaseHandler;
    @MockBean
    CreateUserPort createUserPort;
    @MockBean
    UpdateUserPort updateUserPort;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AesBytesEncryptor aesBytesEncryptor;

    @Test
    void 회원가입_테스트() {
        // given
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserSignUpUseCase.SignUpCommand command = new UserSignUpUseCase.SignUpCommand(loginInfo, userInfo);
        given(createUserPort.creatUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        User newUser = useCaseHandler.signUp(command);
        // then
        Assertions.assertEquals(command.getLoginInfo().getId(), newUser.getLoginInfo().getId());
        Assertions.assertTrue(passwordEncoder.matches(command.getLoginInfo().getPassword(), newUser.getLoginInfo().getPassword()));
        Assertions.assertEquals(command.getUserInfo().getName(), newUser.getUserInfo().getName());
        Assertions.assertEquals(command.getUserInfo().getRegNo(), new String(aesBytesEncryptor.decrypt(HexUtils.fromHexString(newUser.getUserInfo().getRegNo()))));
        then(createUserPort).should().creatUser(any(User.class));
    }

    @Test
    void 회원수정_테스트() {
        // given
        final User.UserId userId = new User.UserId(1L);
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserUpdateUseCase.UpdateCommand command = new UserUpdateUseCase.UpdateCommand(userId, loginInfo, userInfo);
        given(updateUserPort.updateUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        User updatedUser = useCaseHandler.updateAll(command);
        // then
        Assertions.assertEquals(command.getLoginInfo().getId(), updatedUser.getLoginInfo().getId());
        Assertions.assertTrue(passwordEncoder.matches(command.getLoginInfo().getPassword(), updatedUser.getLoginInfo().getPassword()));
        Assertions.assertEquals(command.getUserInfo().getName(), updatedUser.getUserInfo().getName());
        Assertions.assertEquals(command.getUserInfo().getRegNo(), new String(aesBytesEncryptor.decrypt(HexUtils.fromHexString(updatedUser.getUserInfo().getRegNo()))));
        then(updateUserPort).should().updateUser(any(User.class));
    }
}
