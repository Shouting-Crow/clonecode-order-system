package com.clonecode.orderweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDto {

    private Long reviewId;
    private String customerName;
    private Integer rating;
    private String reviewText;
}
