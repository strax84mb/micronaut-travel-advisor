package com.mn.travel.repository;

import com.mn.travel.entity.Airport;
import com.mn.travel.entity.City;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository // Annotation this interface as a DB repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByNameAndCity(String name, City city);

    Optional<Airport> findByAirportId(Long airportId);
}
