package com.clonecode.orderweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterDto {

    private Integer rating;
    private String reviewText;
}
