package com.mn.travel.util;

import com.mn.travel.dto.LoginRequest;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import org.apache.commons.lang3.StringUtils;

public class MTHelper {

    public static String getAdminToken(RxHttpClient client) {
        var loginRequest = LoginRequest.builder()
                .username("admin")
                .password("admin")
                .build();
        var request = HttpRequest.POST("/user/login", loginRequest);
        return client.toBlocking().retrieve(request);
    }

    public static String login(String username, String password, RxHttpClient client) {
        var loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        var request = HttpRequest.POST("/user/login", loginRequest);
        return client.toBlocking().retrieve(request);
    }

    public static boolean signup(String username, String password, RxHttpClient client) {
        var signupRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        var request = HttpRequest.POST("/user/signup", signupRequest);
        return StringUtils.isNotEmpty(client.toBlocking().retrieve(request));
    }
}
