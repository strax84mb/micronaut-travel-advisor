package com.mn.travel.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WriteCommentRequest {

    private String text;
    private Long cityId;
}
