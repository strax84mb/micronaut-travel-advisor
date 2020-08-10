package com.mn.travel.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WriteAirportRequest {

    private Long airportId;
    private String name;
    private String cityName;
    private String cityCountry;
    private String iata;
    private String icao;
}
