package com.mn.travel.exceptions;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String name, String country) {
        super(String.format("City with name %s in country %s not found!", name, country));
    }

    public CityNotFoundException(Long id) {
        super(String.format("City with id %d not found!", id));
    }
}
