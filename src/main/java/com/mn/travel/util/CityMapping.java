package com.mn.travel.util;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CityMapping implements Mapping {

    private int name;
    private int country;
}
