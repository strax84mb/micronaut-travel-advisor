package com.mn.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathRouteDto {

    private String airline;
    private Long airlineId;
    private String airport;
    private String city;
    private String country;
}
