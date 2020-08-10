package com.mn.travel.services.impl;

import com.mn.travel.entity.Airport;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.repository.AirportRepository;
import com.mn.travel.repository.CityRepository;
import com.mn.travel.services.qualifiers.ImportAirports;
import com.mn.travel.util.AirportMapping;
import com.mn.travel.util.Mapping;

import javax.inject.Singleton;

@Singleton
@ImportAirports
public class ImportAirportsService extends AbstractCSVService {

    private CityRepository cityRepository;
    private AirportRepository airportRepository;

    public ImportAirportsService(CityRepository cityRepository, AirportRepository airportRepository) {
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void parseAndSaveLine(String[] fields, Mapping mapping) throws Exception {
        var airportMapping = (AirportMapping) mapping;
        var city = cityRepository.findByNameIgnoreCaseAndCountryIgnoreCase(fields[airportMapping.getCityName()],
                fields[airportMapping.getCityCountry()]);
        if (city.isEmpty()) {
            throw new CityNotFoundException(fields[airportMapping.getCityName()],
                    fields[airportMapping.getCityCountry()]);
        }
        long airportId = toLong(fields[airportMapping.getAirportId()]);
        var optional = airportRepository.findByAirportId(airportId);
        if (optional.isPresent()) {
            optional.ifPresent(airport -> {
                airport.setName(fields[airportMapping.getName()]);
                airport.setCity(city.get());
                airport.setIcao(textOrNull(fields[airportMapping.getIcao()]));
                airport.setIata(textOrNull(fields[airportMapping.getIata()]));
            });
            airportRepository.save(optional.get());
        } else {
            var airport = Airport.builder()
                    .airportId(toLong(fields[airportMapping.getAirportId()]))
                    .name(fields[airportMapping.getName()])
                    .city(city.get())
                    .iata(textOrNull(fields[airportMapping.getIata()]))
                    .icao(textOrNull(fields[airportMapping.getIcao()]))
                    .build();
            airportRepository.save(airport);
        }
    }
}
