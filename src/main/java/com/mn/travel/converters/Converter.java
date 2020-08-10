package com.mn.travel.converters;

public interface Converter<T, K> {

    K convert(T obj);
}
