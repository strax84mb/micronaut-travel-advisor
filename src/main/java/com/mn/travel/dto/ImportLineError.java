package com.mn.travel.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImportLineError {

    private String[] fields;
    private String errorMessage;
}
