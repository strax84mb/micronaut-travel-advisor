package com.mn.travel.services.impl;

import com.mn.travel.dto.PathDto;
import com.mn.travel.repository.AirportRepository;
import com.mn.travel.repository.RouteRepository;
import com.mn.travel.services.CheapestRouteService;
import com.mn.travel.util.path.RouteTree;

import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
@Transactional
public class CheapestRouteServiceImpl implements CheapestRouteService {

    private RouteRepository routeRepository;
    private AirportRepository airportRepository;

    public CheapestRouteServiceImpl(RouteRepository routeRepository, AirportRepository airportRepository) {
        this.routeRepository = routeRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public PathDto findCheapestRoute(Long start, Long destination) {
        var startAirport = airportRepository.findByAirportId(start).get();
        var destinationAirport = airportRepository.findByAirportId(destination).get();
        var tree = new RouteTree(routeRepository, startAirport.getId(), destinationAirport.getId());
        return tree.searchCheapestPath();
    }
}
