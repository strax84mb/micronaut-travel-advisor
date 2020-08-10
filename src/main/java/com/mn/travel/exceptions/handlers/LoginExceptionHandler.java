package com.mn.travel.exceptions.handlers;

import com.mn.travel.exceptions.LoginException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces // Declare that class will provide a response to a request
@Singleton // This will be a singleton bean
@Requires(classes = {LoginException.class}) // Condition needed to call this handler
// Exception handler interface needed to be implemented
public class LoginExceptionHandler implements ExceptionHandler<LoginException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, LoginException exception) {
        // Make a response based on caught exception
        return HttpResponse.unauthorized().body(exception.getMessage());
    }
}
