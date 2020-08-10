package com.mn.travel.services;

import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.exceptions.CityNotFoundException;

import java.util.List;

public interface CityService {

    CityResponse getCity(Long id) throws CityNotFoundException;

    CityResponse getCity(Long id, Integer maxComments) throws CityNotFoundException;

    CityResponse addCity(WriteCityRequest dto);

    CityResponse updateCity(Long id, WriteCityRequest dto);

    List<CityResponse> listAllCities();

    List<CityResponse> listAllCities(Integer maxComments);
}
