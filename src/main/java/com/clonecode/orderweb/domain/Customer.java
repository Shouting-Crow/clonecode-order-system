package com.clonecode.orderweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;

    @Embedded
    private Address billingAddress;

    @Embedded
    private Address deliveryAddress;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

}
