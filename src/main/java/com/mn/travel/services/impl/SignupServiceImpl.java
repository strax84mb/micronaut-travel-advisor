package com.mn.travel.services.impl;

import com.mn.travel.converters.Converter;
import com.mn.travel.converters.qualifiers.LoginRequesToUser;
import com.mn.travel.dto.LoginRequest;
import com.mn.travel.entity.User;
import com.mn.travel.entity.UserRole;
import com.mn.travel.repository.UserRepository;
import com.mn.travel.services.SignupService;
import com.mn.travel.util.EncoderUtil;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;

/**
 * Service used for signing up a new user.
 * @Singleton designates this bean as singleton
 */
@Singleton
public class SignupServiceImpl implements SignupService {

    private UserRepository userRepository;
    private Converter<LoginRequest, User> converter;
    private EncoderUtil encoderUtil;

    /**
     * Constructor used to instantiate this class
     * @param userRepository DB user repository
     * @param converter Converter used to convert request dto to DB entity
     * @param encoderUtil Class used to encode password. Password will not be saved in raw form
     */
    public SignupServiceImpl(UserRepository userRepository,
                             // @LoginRequesToUser is a qualifier used to connect
                             // Converter interface to a proper implementation
                             @LoginRequesToUser Converter<LoginRequest, User> converter,
                             EncoderUtil encoderUtil) {
        this.userRepository = userRepository;
        this.converter = converter;
        this.encoderUtil = encoderUtil;
    }

    /**
     * Used to sign up a new user
     * @param username
     * @param password
     * @return ID of a new user
     * @throws NoSuchAlgorithmException
     */
    @Override
    public Long signup(String username, String password) throws NoSuchAlgorithmException {
        // Generate a random salt used to encode password
        // We will not provide
        var salt = encoderUtil.generateSalt();
        var user = User.builder()
                .username(username)
                .salt(salt)
                .role(UserRole.USER)
                .password(encoderUtil.encode(password, salt))
                .build();
        // Save user
        user = userRepository.save(user);
        return user.getId();
    }
}
