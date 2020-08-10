package com.mn.travel.repository;

import com.mn.travel.entity.Airport;
import com.mn.travel.entity.Route;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository // Annotation this interface as a DB repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByAirlineIdAndSourceAndDestination(Long airlineId, Airport source, Airport destination);

    List<Route> findBySourceIdAndDestinationIdNotIn(Long sourcerId, List<Long> destinationIds);
}
