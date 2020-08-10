package com.mn.travel.services.impl;

import com.mn.travel.entity.Route;
import com.mn.travel.exceptions.AirportNotFoundException;
import com.mn.travel.repository.AirportRepository;
import com.mn.travel.repository.RouteRepository;
import com.mn.travel.services.qualifiers.ImportRoutes;
import com.mn.travel.util.Mapping;
import com.mn.travel.util.RouteMapping;

import javax.inject.Singleton;

@Singleton
@ImportRoutes
public class ImportRoutesService extends AbstractCSVService {

    private RouteRepository routeRepository;
    private AirportRepository airportRepository;

    public ImportRoutesService(RouteRepository routeRepository, AirportRepository airportRepository) {
        this.routeRepository = routeRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public void parseAndSaveLine(String[] fields, Mapping mapping) throws Exception {
        var routeMapping = (RouteMapping) mapping;
        long sourceAirportId = toLong(fields[routeMapping.getSourceAirportId()]);
        long destinationAirportId = toLong(fields[routeMapping.getDestinationAirportId()]);
        var source = airportRepository.findByAirportId(sourceAirportId);
        if (source.isEmpty()) {
            throw new AirportNotFoundException(sourceAirportId);
        }
        var destination = airportRepository.findByAirportId(destinationAirportId);
        if (destination.isEmpty()) {
            throw new AirportNotFoundException(destinationAirportId);
        }
        long airlineId = toLong(fields[routeMapping.getAirlineId()]);
        var routeOptional = routeRepository.findByAirlineIdAndSourceAndDestination(
                airlineId, source.get(), destination.get());
        if (routeOptional.isPresent()) {
            routeOptional.ifPresent(route -> {
                route.setPrice(toDouble(fields[routeMapping.getPrice()]));
            });
            routeRepository.save(routeOptional.get());
        } else {
            var route = Route.builder()
                    .airline(fields[routeMapping.getAirline()])
                    .airlineId(airlineId)
                    .source(source.get())
                    .destination(destination.get())
                    .price(toDouble(fields[routeMapping.getPrice()]))
                    .build();
            routeRepository.save(route);
        }
    }
}
