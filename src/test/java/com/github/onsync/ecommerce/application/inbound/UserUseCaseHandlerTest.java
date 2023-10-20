package com.github.onsync.ecommerce.application.inbound;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.outbound.CreateUserPort;
import com.github.onsync.ecommerce.application.outbound.LoadUserPort;
import com.github.onsync.ecommerce.application.outbound.UpdateUserPort;
import jakarta.persistence.EntityExistsException;
import org.apache.tomcat.util.buf.HexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;

@SpringBootTest
public class UserUseCaseHandlerTest {

    @Autowired
    UserUseCaseHandler useCaseHandler;
    @MockBean
    CreateUserPort createUserPort;
    @MockBean
    UpdateUserPort updateUserPort;
    @MockBean
    LoadUserPort loadUserPort;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AesBytesEncryptor aesBytesEncryptor;

    @Test
    void 회원가입_테스트_성공() {
        // given
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserSignUpUseCase.SignUpCommand command = new UserSignUpUseCase.SignUpCommand(loginInfo, userInfo);
        final User expectedUser = new User(null, loginInfo, userInfo);
        given(loadUserPort.findByLoginId(any(String.class))).willReturn(Optional.of(expectedUser));
        given(createUserPort.creatUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        User newUser = useCaseHandler.signUp(command);
        // then
        Assertions.assertEquals(command.getLoginInfo().getId(), newUser.getLoginInfo().getId());
        Assertions.assertTrue(passwordEncoder.matches(command.getLoginInfo().getPassword(), newUser.getLoginInfo().getPassword()));
        Assertions.assertEquals(command.getUserInfo().getName(), newUser.getUserInfo().getName());
        Assertions.assertEquals(command.getUserInfo().getRegNo(), new String(aesBytesEncryptor.decrypt(HexUtils.fromHexString(newUser.getUserInfo().getRegNo()))));
        then(createUserPort).should(only()).creatUser(any(User.class));
    }

    @Test
    void 회원가입_테스트_실패_EntityExistsException() {
        // given
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserSignUpUseCase.SignUpCommand command = new UserSignUpUseCase.SignUpCommand(loginInfo, userInfo);
        given(loadUserPort.findByLoginId(any(String.class))).willReturn(Optional.empty());
        given(createUserPort.creatUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        Executable executeSingUp = () -> useCaseHandler.signUp(command);
        // then
        Assertions.assertThrows(EntityExistsException.class, executeSingUp);
    }

    @Test
    void 회원수정_테스트_성공() {
        // given
        final User.UserId userId = new User.UserId(1L);
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserUpdateUseCase.UpdateCommand command = new UserUpdateUseCase.UpdateCommand(userId, loginInfo, userInfo);
        final User expectedUser = new User(userId, loginInfo, userInfo);
        given(loadUserPort.findByUserId(any(User.UserId.class))).willReturn(Optional.of(expectedUser));
        given(updateUserPort.updateUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        User updatedUser = useCaseHandler.updateAll(command);
        // then
        Assertions.assertEquals(command.getUserId().getValue(), updatedUser.getUserId().getValue());
        Assertions.assertEquals(command.getLoginInfo().getId(), updatedUser.getLoginInfo().getId());
        Assertions.assertTrue(passwordEncoder.matches(command.getLoginInfo().getPassword(), updatedUser.getLoginInfo().getPassword()));
        Assertions.assertEquals(command.getUserInfo().getName(), updatedUser.getUserInfo().getName());
        Assertions.assertEquals(command.getUserInfo().getRegNo(), new String(aesBytesEncryptor.decrypt(HexUtils.fromHexString(updatedUser.getUserInfo().getRegNo()))));
        then(updateUserPort).should(only()).updateUser(any(User.class));
    }

    @Test
    void 회원수정_테스트_실패_NoSuchElementException() {
        // given
        final User.UserId userId = new User.UserId(1L);
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final User.UserInfo userInfo = new User.UserInfo("username", "regNo");
        final UserUpdateUseCase.UpdateCommand command = new UserUpdateUseCase.UpdateCommand(userId, loginInfo, userInfo);
        given(loadUserPort.findByUserId(any(User.UserId.class))).willReturn(Optional.empty());
        given(updateUserPort.updateUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        Executable executeUpdateAll = () -> useCaseHandler.updateAll(command);
        // then
        Assertions.assertThrows(NoSuchElementException.class, executeUpdateAll);
    }

    @Test
    void 유저탈퇴_테스트_성공() {
        // given
        final User.UserId userId = new User.UserId(1L);
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final UserResignUseCase.ResignCommand command = new UserResignUseCase.ResignCommand(userId, loginInfo);
        final User expectedUser = new User(userId, loginInfo, null);
        given(loadUserPort.findByLoginId(any(String.class))).willReturn(Optional.of(expectedUser));
        given(updateUserPort.updateUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        User updatedUser = useCaseHandler.resign(command);
        // then
        Assertions.assertEquals(command.getUserId().getValue(), updatedUser.getUserId().getValue());
        Assertions.assertEquals(command.getLoginInfo().getId(), updatedUser.getLoginInfo().getId());
        Assertions.assertTrue(passwordEncoder.matches(command.getLoginInfo().getPassword(), updatedUser.getLoginInfo().getPassword()));
        then(loadUserPort).should(only()).findByLoginId(any(String.class));
        then(updateUserPort).should(only()).updateUser(any(User.class));
    }

    @Test
    void 유저탈퇴_테스트_실패_NoSuchElementException() {
        // given
        final User.UserId userId = new User.UserId(1L);
        final User.LoginInfo loginInfo = new User.LoginInfo("loginId", "password");
        final UserResignUseCase.ResignCommand command = new UserResignUseCase.ResignCommand(userId, loginInfo);
        given(loadUserPort.findByLoginId(any(String.class))).willReturn(Optional.empty());
        given(updateUserPort.updateUser(any(User.class))).willAnswer(p -> p.getArgument(0));
        // when
        Executable executeResign = () -> useCaseHandler.resign(command);
        // then
        Assertions.assertThrows(NoSuchElementException.class, executeResign);
    }
}
