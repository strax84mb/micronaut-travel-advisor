package com.mn.travel.controllers;

import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.services.CityService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller("/city")
public class CityController {

    private final CityService cityService;

    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public CityResponse addCity(@Body WriteCityRequest dto) {
        return cityService.addCity(dto);
    }

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public CityResponse updateCity(@PathVariable("id") Long id, @Body WriteCityRequest dto) {
        return cityService.updateCity(id, dto);
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public CityResponse getCity(@PathVariable("id") Long id,
                                @QueryValue(value = "max-comments", defaultValue = "-1") int maxComments) throws CityNotFoundException {
        return maxComments == -1 ? cityService.getCity(id) : cityService.getCity(id, maxComments);
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public List<CityResponse> listAllCities(@QueryValue(value = "max-comments", defaultValue = "-1") int maxComments) {
        return maxComments == -1 ? cityService.listAllCities() : cityService.listAllCities(maxComments);
    }
}
