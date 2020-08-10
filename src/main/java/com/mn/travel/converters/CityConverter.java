package com.mn.travel.converters;

import com.mn.travel.entity.City;

public interface CityConverter<T> extends Converter<City, T> {

    T convert(City obj, int maxComments);
}
