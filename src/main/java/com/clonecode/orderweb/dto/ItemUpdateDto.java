package com.clonecode.orderweb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemUpdateDto extends ItemRegisterDto{
    private Long id;
}
