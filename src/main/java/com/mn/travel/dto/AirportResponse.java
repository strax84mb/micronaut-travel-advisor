package com.mn.travel.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AirportResponse {

    private String name;
    private CityResponse city;
    private String iata;
    private String icao;
}
