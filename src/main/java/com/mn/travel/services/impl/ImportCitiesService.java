package com.mn.travel.services.impl;

import com.mn.travel.entity.City;
import com.mn.travel.repository.CityRepository;
import com.mn.travel.services.qualifiers.ImportCities;
import com.mn.travel.util.CityMapping;
import com.mn.travel.util.Mapping;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@ImportCities
public class ImportCitiesService extends AbstractCSVService {

    private CityRepository cityRepository;

    public ImportCitiesService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void parseAndSaveLine(String[] fields, Mapping mapping) throws Exception {
        var cityMapping = (CityMapping) mapping;
        var cityOptional = cityRepository.findByNameIgnoreCaseAndCountryIgnoreCase(fields[cityMapping.getName()],
                fields[cityMapping.getCountry()]);
        if (cityOptional.isEmpty()) {
            cityRepository.save(City.builder()
                    .name(fields[cityMapping.getName()])
                    .country(fields[cityMapping.getCountry()])
                    .comments(List.of())
                    .build());
        }
    }
}
