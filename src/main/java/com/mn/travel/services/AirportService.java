package com.mn.travel.services;

import com.mn.travel.dto.AirportResponse;
import com.mn.travel.dto.WriteAirportRequest;
import com.mn.travel.exceptions.AirportNotFoundException;
import com.mn.travel.exceptions.CityNotFoundException;

import java.util.List;

public interface AirportService {

    AirportResponse getById(Long id);

    AirportResponse addAirport(WriteAirportRequest dto) throws CityNotFoundException;

    AirportResponse updateAirport(Long id, WriteAirportRequest dto) throws CityNotFoundException, AirportNotFoundException;

    List<AirportResponse> listAirports();
}
