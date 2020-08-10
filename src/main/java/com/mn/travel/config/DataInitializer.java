package com.mn.travel.config;

import com.mn.travel.entity.User;
import com.mn.travel.entity.UserRole;
import com.mn.travel.repository.UserRepository;
import com.mn.travel.util.EncoderUtil;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Async;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;

/**
 * Class used to fill in initial data after service startup is finished
 */
@Singleton
public class DataInitializer {

    private UserRepository userRepository;
    private EncoderUtil encoderUtil;

    /**
     * Controller used to instantiate this class
     * @param userRepository
     * @param encoderUtil Class used to encode password. Password will not be saved in raw form
     */
    public DataInitializer(UserRepository userRepository, EncoderUtil encoderUtil) {
        this.userRepository = userRepository;
        this.encoderUtil = encoderUtil;
    }

    /**
     * Method to be executed when service finishes startup.
     * @{@link Async} Indicates method will be executed in a separate thread.
     * @{@link EventListener} Indicates thias method will depend on an event
     * @param event Event representing that service had started up
     * @throws NoSuchAlgorithmException
     */
    @EventListener
    @Async
    public void initAdmins(final ServiceReadyEvent event) throws NoSuchAlgorithmException {
        var salt = encoderUtil.generateSalt();
        var admin = User.builder()
                .username("admin")
                .password(encoderUtil.encode("admin", salt))
                .role(UserRole.ADMIN)
                .salt(salt)
                .build();
        userRepository.save(admin);
    }
}
