package com.mn.travel.exceptions.handlers;

import com.mn.travel.exceptions.AirportNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces // Declare that class will provide a response to a request
@Singleton // This will be a singleton bean
@Requires(classes = AirportNotFoundException.class) // Condition needed to call this handler
public class AirportNotFoundExceptionHandler implements ExceptionHandler<AirportNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, AirportNotFoundException exception) {
        // Make a response based on caught exception
        return HttpResponse.notFound(exception.getMessage());
    }
}
