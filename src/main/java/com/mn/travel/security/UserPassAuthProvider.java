package com.mn.travel.security;

import com.mn.travel.exceptions.IncorrectCredentialsException;
import com.mn.travel.exceptions.UsernameNotFoundException;
import com.mn.travel.repository.UserRepository;
import com.mn.travel.util.EncoderUtil;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Implementation of {@link AuthenticationProvider} used for authenticating login request
 */
@Singleton
@Transactional
public class UserPassAuthProvider implements AuthenticationProvider {

    private EncoderUtil encoderUtil;
    private UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            var userOptional = userRepository.findByUsername(authenticationRequest.getIdentity().toString());
            if (userOptional.isPresent()) {
                var user = userOptional.get();
                var pass = encoderUtil.encode(authenticationRequest.getSecret().toString(), user.getSalt());
                if (pass.equals(user.getPassword())) {
                    var roles = List.of(user.getRole().name());
                    emitter.onNext(new UserDetails((String) authenticationRequest.getIdentity(), roles));
                    emitter.onComplete();
                } else {
                    emitter.onError(new IncorrectCredentialsException());
                }
            } else {
                emitter.onError(new UsernameNotFoundException(authenticationRequest.getIdentity().toString()));
            }
        }, BackpressureStrategy.ERROR);
    }
}
