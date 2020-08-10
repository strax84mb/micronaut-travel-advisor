package com.mn.travel.util;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AirportMapping implements Mapping {

    private int airportId;
    private int name;
    private int cityName;
    private int cityCountry;
    private int iata;
    private int icao;
}
