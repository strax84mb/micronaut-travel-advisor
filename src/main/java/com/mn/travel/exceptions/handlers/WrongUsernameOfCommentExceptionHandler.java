package com.mn.travel.exceptions.handlers;

import com.mn.travel.exceptions.WrongUsernameOfCommentException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;


@Produces // Declare that class will provide a response to a request
@Singleton // This will be a singleton bean
@Requires(classes = {WrongUsernameOfCommentException.class}) // Condition needed to call this handler
public class WrongUsernameOfCommentExceptionHandler implements
        ExceptionHandler<WrongUsernameOfCommentException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, WrongUsernameOfCommentException exception) {
        // Make a response based on caught exception
        return HttpResponse.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
