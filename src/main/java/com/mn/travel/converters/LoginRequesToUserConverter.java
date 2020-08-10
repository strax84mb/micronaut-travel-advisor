package com.mn.travel.converters;

import com.mn.travel.converters.qualifiers.LoginRequesToUser;
import com.mn.travel.dto.LoginRequest;
import com.mn.travel.entity.User;

import javax.inject.Singleton;

@Singleton
@LoginRequesToUser
public class LoginRequesToUserConverter implements Converter<LoginRequest, User> {

    @Override
    public User convert(LoginRequest obj) {
        return User.builder()
                .username(obj.getUsername())
                .password(obj.getPassword())
                .build();
    }
}
