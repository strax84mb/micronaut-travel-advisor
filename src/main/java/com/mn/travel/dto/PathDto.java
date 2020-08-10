package com.mn.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathDto {

    private Double sumPrice;
    private Integer numberOfFlights;
    private PathRouteDto start;
    private List<PathRouteDto> flights;
}
