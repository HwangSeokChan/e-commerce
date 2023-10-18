package com.github.onsync.ecommerce.application.config;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.application.inbound.UserSignUpUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.HexUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@Configuration
@AutoConfigureAfter(AppEncryptorConfig.class)
@RequiredArgsConstructor
public class AppModelMapperConfig {

    private final PasswordEncoder passwordEncoder;
    private final AesBytesEncryptor aesBytesEncryptor;

    @Bean
    public ModelMapper appModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(signUpCommand2User());
        return modelMapper;
    }

    private Converter<UserSignUpUseCase.SignUpCommand, User> signUpCommand2User() {
        return new AbstractConverter<>() {
            @Override
            protected User convert(UserSignUpUseCase.SignUpCommand source) {

                final String encryptedPassword = passwordEncoder.encode(source.getLoginInfo().getPassword());
                final User.LoginInfo loginInfo = new User.LoginInfo(source.getLoginInfo().getId(), encryptedPassword);

                final String encryptedRegNo = HexUtils.toHexString(aesBytesEncryptor.encrypt(source.getUserInfo().getRegNo().getBytes(StandardCharsets.UTF_8)));
                final User.UserInfo userInfo = new User.UserInfo(source.getUserInfo().getName(), encryptedRegNo);

                return new User(null, loginInfo, userInfo);
            }
        };
    }
}
