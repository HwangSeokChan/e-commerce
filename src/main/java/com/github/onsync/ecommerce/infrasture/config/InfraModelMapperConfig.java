package com.github.onsync.ecommerce.infrasture.config;

import com.github.onsync.ecommerce.application.domain.User;
import com.github.onsync.ecommerce.infrasture.jpa.UserData;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfraModelMapperConfig {

    @Bean
    public ModelMapper infraModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(userDomain2Data());
        modelMapper.addConverter(userData2Domain());
        return modelMapper;
    }

    private Converter<UserData, User> userData2Domain() {
        return new AbstractConverter<>() {
            @Override
            protected User convert(UserData source) {
                final User.UserId userId = new User.UserId(source.getId());
                final User.LoginInfo loginInfo = new User.LoginInfo(source.getLoginId(), source.getPassword());
                final User.UserInfo userInfo = new User.UserInfo(source.getName(), source.getRegNo());

                return new User(userId, loginInfo, userInfo);
            }
        };
    }

    private Converter<User, UserData> userDomain2Data() {
        return new AbstractConverter<>() {
            @Override
            protected UserData convert(User source) {
                final User.UserId userId = source.getUserId();
                final User.LoginInfo loginInfo = source.getLoginInfo();
                final User.UserInfo userInfo = source.getUserInfo();

                return new UserData(userId == null ? null : userId.getValue(),
                        loginInfo.getId(), loginInfo.getPassword(),
                        userInfo.getName(), userInfo.getRegNo());
            }
        };
    }
}
