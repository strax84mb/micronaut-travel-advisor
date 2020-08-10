package com.mn.travel.converters;

import com.mn.travel.converters.qualifiers.AirportToAirportResponseQualifier;
import com.mn.travel.converters.qualifiers.CityToCityResponseQualifier;
import com.mn.travel.dto.AirportResponse;
import com.mn.travel.dto.CityResponse;
import com.mn.travel.entity.Airport;

import javax.inject.Singleton;

@Singleton
@AirportToAirportResponseQualifier
public class AirportToAirportResponseConverter implements Converter<Airport, AirportResponse> {

    private CityConverter<CityResponse> cityConverter;

    public AirportToAirportResponseConverter(@CityToCityResponseQualifier CityConverter<CityResponse> cityConverter) {
        this.cityConverter = cityConverter;
    }

    @Override
    public AirportResponse convert(Airport airport) {
        return AirportResponse.builder()
                .name(airport.getName())
                .iata(airport.getIata())
                .icao(airport.getIcao())
                .city(cityConverter.convert(airport.getCity(), 0))
                .build();
    }
}
