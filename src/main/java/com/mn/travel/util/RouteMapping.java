package com.mn.travel.util;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RouteMapping implements Mapping {

    private int airline;
    private int airlineId;
    private int sourceAirportId;
    private int destinationAirportId;
    private int price;
}
