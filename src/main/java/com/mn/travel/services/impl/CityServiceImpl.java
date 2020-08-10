package com.mn.travel.services.impl;

import com.mn.travel.converters.CityConverter;
import com.mn.travel.converters.qualifiers.CityToCityResponseQualifier;
import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.WriteCityRequest;
import com.mn.travel.entity.City;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.repository.CityRepository;
import com.mn.travel.services.CityService;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Singleton
@Transactional
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;
    private CityConverter<CityResponse> converter;

    public CityServiceImpl(CityRepository cityRepository,
                           @CityToCityResponseQualifier CityConverter<CityResponse> converter) {
        this.cityRepository = cityRepository;
        this.converter = converter;
    }

    @Override
    public CityResponse getCity(Long id) throws CityNotFoundException {
        return getCity(id, null);
    }

    @Override
    public CityResponse getCity(Long id, Integer maxComments) throws CityNotFoundException {
        var city = cityRepository.findById(id);
        if (city.isPresent()) {
            return maxComments == null ? converter.convert(city.get()) : converter.convert(city.get(), maxComments);
        } else {
            throw new CityNotFoundException(id);
        }
    }

    @Override
    public CityResponse addCity(WriteCityRequest dto) {
        var city = City.builder()
                .name(dto.getName())
                .country(dto.getCountry())
                .comments(List.of())
                .build();
        city = cityRepository.save(city);
        return converter.convert(city);
    }

    @Override
    public CityResponse updateCity(Long id, WriteCityRequest dto) {
        var cityOptional = cityRepository.findById(id);
        var response = new AtomicReference((CityResponse) null);
        cityOptional.ifPresent(city -> {
            city.setName(dto.getName());
            city.setCountry(dto.getCountry());
            var result = cityRepository.save(city);
            response.set(converter.convert(result));
        });
        return (CityResponse) response.get();
    }

    @Override
    public List<CityResponse> listAllCities() {
        return cityRepository.findAll().stream()
                .map(city -> converter.convert(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<CityResponse> listAllCities(Integer maxComments) {
        return cityRepository.findAll().stream()
                .map(city -> converter.convert(city, maxComments))
                .collect(Collectors.toList());
    }
}
