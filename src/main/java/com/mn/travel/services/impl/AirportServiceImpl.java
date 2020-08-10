package com.mn.travel.services.impl;

import com.mn.travel.converters.Converter;
import com.mn.travel.converters.qualifiers.AirportToAirportResponseQualifier;
import com.mn.travel.dto.AirportResponse;
import com.mn.travel.dto.WriteAirportRequest;
import com.mn.travel.entity.Airport;
import com.mn.travel.entity.City;
import com.mn.travel.exceptions.AirportNotFoundException;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.repository.AirportRepository;
import com.mn.travel.repository.CityRepository;
import com.mn.travel.services.AirportService;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class AirportServiceImpl implements AirportService {

    private AirportRepository airportRepository;
    private CityRepository cityRepository;
    private Converter<Airport, AirportResponse> converter;

    public AirportServiceImpl(AirportRepository airportRepository,
                              CityRepository cityRepository,
                              @AirportToAirportResponseQualifier  Converter<Airport, AirportResponse> converter) {
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
        this.converter = converter;
    }

    @Override
    public AirportResponse getById(Long id) {
        var airport = airportRepository.findById(id)
                .orElseThrow(() -> new AirportNotFoundException(id));
        return converter.convert(airport);
    }

    @Override
    public AirportResponse addAirport(WriteAirportRequest dto) throws CityNotFoundException {
        var city = findCity(dto);
        var airport = Airport.builder()
                .airportId(dto.getAirportId())
                .name(dto.getName())
                .iata(dto.getIata())
                .icao(dto.getIcao())
                .city(city)
                .build();
        airport = airportRepository.save(airport);
        return converter.convert(airport);
    }

    @Override
    public AirportResponse updateAirport(Long id, WriteAirportRequest dto) throws CityNotFoundException, AirportNotFoundException {
        var city = findCity(dto);
        var airport = airportRepository.findById(id);
        if (airport.isEmpty()) {
            throw new AirportNotFoundException(id);
        }
        airport.ifPresent(entity -> {
            entity.setAirportId(dto.getAirportId());
            entity.setIata(dto.getIata());
            entity.setIcao(dto.getIcao());
            entity.setName(dto.getName());
            entity.setCity(city);
        });
        var savedEntity = airportRepository.save(airport.get());
        return converter.convert(savedEntity);
    }

    private City findCity(WriteAirportRequest dto) throws CityNotFoundException {
        var city = cityRepository.findByNameIgnoreCaseAndCountryIgnoreCase(dto.getCityName(), dto.getCityCountry());
        if (city.isEmpty()) {
            throw new CityNotFoundException(dto.getCityName(), dto.getCityCountry());
        }
        return city.get();
    }

    @Override
    public List<AirportResponse> listAirports() {
        return airportRepository.findAll().stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }
}
