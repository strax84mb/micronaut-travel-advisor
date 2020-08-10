package com.mn.travel.controllers;

import com.mn.travel.dto.PathDto;
import com.mn.travel.services.CheapestRouteService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
 * Controller for /travel/cheapest endpoint
 */
@Controller("/travel/cheapest")
public class TravelController {

    private CheapestRouteService cheapestRouteService;

    /**
     * Controller used to instantiate this class
     * @param cheapestRouteService
     */
    public TravelController(CheapestRouteService cheapestRouteService) {
        this.cheapestRouteService = cheapestRouteService;
    }

    /**
     * Handler for GET request on endpoint /travel/cheapest?start=123&end=456
     * @param startingAirportId Airport ID of starting point
     * @param endingAirportId Airport ID of finish point
     * @return
     */
    @Get
    // Request must have a valid bearer JWT in Authorization header
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public PathDto findCheapestRoute(@QueryValue("start") Long startingAirportId,
                                     @QueryValue("end") Long endingAirportId) {
        return cheapestRouteService.findCheapestRoute(startingAirportId, endingAirportId);
    }
}
