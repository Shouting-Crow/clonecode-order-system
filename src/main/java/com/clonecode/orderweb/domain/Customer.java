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
    private String loginId;
    private String password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "billing_street_address"))
    })
    private Address billingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "delivery_city")),
            @AttributeOverride(name = "streetAddress", column = @Column(name = "delivery_street_address"))
    })
    private Address deliveryAddress;

    @OneToMany(mappedBy = "customer")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

}
