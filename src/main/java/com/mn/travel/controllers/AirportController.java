package com.mn.travel.controllers;

import com.mn.travel.dto.AirportResponse;
import com.mn.travel.dto.WriteAirportRequest;
import com.mn.travel.exceptions.AirportNotFoundException;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.services.AirportService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

/**
 * Controller for /city/airport endpoint
 */
@Controller("/city/airport")
public class AirportController {

    private AirportService airportService;

    /**
     * Controller used to instantiate this class
     * @param airportService
     */
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    /**
     * GET request handler for endpoint /city/airport/123
     * @param id // Entity ID of airport
     * @return
     */
    @Get("/{id}")
    // Method will return a JSON
    @Produces(MediaType.APPLICATION_JSON)
    // Request must be made by logged in user
    @Secured(SecurityRule.IS_AUTHENTICATED)
    AirportResponse getById(@PathVariable("id") Long id) {
        return airportService.getById(id);
    }

    /**
     * POST request handler for endpoint /city/airport
     * This is how new airport is saved
     * @param dto Airport to be saved
     * @return
     * @throws CityNotFoundException Thrown if airport references a non existent city
     */
    @Post
    // Body must be a JSON
    @Consumes(MediaType.APPLICATION_JSON)
    // Method returns a JSON
    @Produces(MediaType.APPLICATION_JSON)
    // Only administrators can add an airport
    @Secured("ADMIN")
    AirportResponse addAirport(@Body WriteAirportRequest dto) throws CityNotFoundException {
        return airportService.addAirport(dto);
    }

    /**
     * PUT request handler for endpoint /city/airport/123
     * This is how airport data is changed
     * @param id Entity ID of airport
     * @param dto Airport data to be saved
     * @return
     * @throws CityNotFoundException Thrown if airport references a non existent city
     * @throws AirportNotFoundException Thrown if requested airport does not exist
     */
    @Put("/{id}")
    // Body must be a JSON
    @Consumes(MediaType.APPLICATION_JSON)
    // Method returns a JSON
    @Produces(MediaType.APPLICATION_JSON)
    // Only administrators can change airport data
    @Secured("ADMIN")
    AirportResponse updateAirport(@PathVariable("id") Long id, WriteAirportRequest dto)
            throws CityNotFoundException, AirportNotFoundException {
        return airportService.updateAirport(id, dto);
    }

    /**
     * GET request handler for endpoint /city/airport
     * This is how all airports are listed
     * @return
     */
    @Get
    // Method returns a JSON
    @Produces(MediaType.APPLICATION_JSON)
    // Request must have a valid bearer JWT in Authorization header
    @Secured(SecurityRule.IS_AUTHENTICATED)
    List<AirportResponse> listAirports() {
        return airportService.listAirports();
    }
}
