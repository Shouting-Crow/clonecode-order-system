package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.Address;
import com.clonecode.orderweb.domain.DeliveryStatus;
import lombok.Data;

@Data
public class DeliveryDto {
    private DeliveryStatus deliveryStatus;
    private String deliveryAddress;
}
