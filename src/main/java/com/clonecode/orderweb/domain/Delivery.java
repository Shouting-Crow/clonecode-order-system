package com.clonecode.orderweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
