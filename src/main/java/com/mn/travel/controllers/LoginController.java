package com.mn.travel.controllers;

import com.mn.travel.dto.LoginRequest;
import com.mn.travel.exceptions.LoginException;
import com.mn.travel.services.SignupService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;

import java.security.NoSuchAlgorithmException;

/**
 * Controller for /user endpoint
 */
@Controller("/user")
public class LoginController {

    private SignupService signupService;
    private AppClient appClient;

    /**
     * Controller used to instantiate this class
     * @param signupService Our service used to sign up a user
     * @param appClient Client used to access /login endpoint (provided out of the box)
     */
    public LoginController(SignupService signupService, AppClient appClient) {
        this.signupService = signupService;
        this.appClient = appClient;
    }

    /**
     * POST request handler for endpoint /user/login
     * @param payload Request payload
     * @return JWT access token
     */
    @Post("/login")
    // Body must be a JSON
    @Consumes(MediaType.APPLICATION_JSON)
    // Method will return a string
    @Produces(MediaType.TEXT_PLAIN)
    // Request must be made by anyone
    @Secured(SecurityRule.IS_ANONYMOUS)
    // Request body is designated by @Body
    public String login(@Body LoginRequest payload) {
        try {
            // Create a payload for login
            var creds = new UsernamePasswordCredentials(
                    payload.getUsername(),
                    payload.getPassword());
            // Attempt login
            // Authentication will be performed in class com.mn.travel.security.UserPassAuthProvider
            var token = appClient.login(creds);
            return token.getAccessToken();
        } catch (Exception e) {
            /* Catch all exception thrown during login attempt
               We do it this way since all exceptions thrown in UserPassAuthProvider
               will be wrapped in InternalServerErrorException and we want to use
               exception handler in order to change response of this method.
               Exception handler used is com.mn.travel.exceptions.handlers.LoginExceptionHandler
             */
            throw new LoginException(e.getMessage().substring("Internal Server Error: ".length()));
        }
    }

    /**
     * POST request handler for endpoint /user/signup
     * @param payload Request payload
     * @return ID of newly created user
     * @throws NoSuchAlgorithmException
     */
    @Post("/signup")
    // Body must be a JSON
    @Consumes(MediaType.APPLICATION_JSON)
    // Method will return a string
    @Produces(MediaType.TEXT_PLAIN)
    // Request must be made by anyone
    @Secured(SecurityRule.IS_ANONYMOUS)
    // Request body is designated by @Body
    public String signup(@Body LoginRequest payload) throws NoSuchAlgorithmException {
        return signupService.signup(payload.getUsername(), payload.getPassword()).toString();
    }
}
