package com.mn.travel.repository;

import com.mn.travel.entity.City;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository // Annotation this interface as a DB repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByNameIgnoreCaseAndCountryIgnoreCase(String name, String country);
}
