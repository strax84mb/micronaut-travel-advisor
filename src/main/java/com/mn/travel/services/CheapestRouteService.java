package com.mn.travel.services;

import com.mn.travel.dto.PathDto;

public interface CheapestRouteService {

    PathDto findCheapestRoute(Long start, Long destination);
}
