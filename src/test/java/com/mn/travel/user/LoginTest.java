package com.mn.travel.user;

import com.mn.travel.dto.LoginRequest;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class LoginTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    public void loginWrongUsername() {
        var loginRequest = LoginRequest.builder()
                .username("non_user")
                .password("non_password")
                .build();
        var request = HttpRequest.POST("/user/login", loginRequest);
        assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request),
                "Internal Server Error: Username not found!");
    }

    @Test
    public void loginWrongPassword() {
        var loginRequest = LoginRequest.builder()
                .username("admin")
                .password("wrong_password")
                .build();
        var request = HttpRequest.POST("/user/login", loginRequest);
        assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(request),
                "Internal Server Error: Provided credentials are incorrect!");
    }

    @Test
    public void loginAdmin() throws ParseException {
        var loginRequest = LoginRequest.builder()
                .username("admin")
                .password("admin")
                .build();
        var request = HttpRequest.POST("/user/login", loginRequest);
        var token = client.toBlocking().retrieve(request);
        var jwt = JWTParser.parse(token);
        var roles = (JSONArray) jwt.getJWTClaimsSet().getClaims().get("roles");
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0));
    }

    @Test
    public void userSignupAndLogin() throws ParseException {
        // First sign up
        var loginRequest = LoginRequest.builder()
                .username("user")
                .password("user")
                .build();
        var request = HttpRequest.POST("/user/signup", loginRequest);
        var userId = client.toBlocking().retrieve(request);
        assertEquals("2", userId);
        // Then login
        request = HttpRequest.POST("/user/login", loginRequest);
        var token = client.toBlocking().retrieve(request);
        // Then validate
        var jwt = JWTParser.parse(token);
        var roles = (JSONArray) jwt.getJWTClaimsSet().getClaims().get("roles");
        assertEquals(1, roles.size());
        assertEquals("USER", roles.get(0));
        assertEquals("user", jwt.getJWTClaimsSet().getSubject());
    }
}
