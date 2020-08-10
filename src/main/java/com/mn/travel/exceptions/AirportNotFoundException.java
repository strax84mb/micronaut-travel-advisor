package com.mn.travel.exceptions;

public class AirportNotFoundException extends RuntimeException {

    public AirportNotFoundException(Long id) {
        super(String.format("Airport with airport ID %d not found!", id));
    }
}
