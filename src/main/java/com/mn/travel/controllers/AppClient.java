package com.mn.travel.controllers;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

// Client used to access an endpoint
// In this case we are accessing out of the box endpoint
// in order to login to our service
@Client("/")
public interface AppClient {

    // Endpoint used for login
    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials creds);
}
