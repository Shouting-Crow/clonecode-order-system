package com.clonecode.orderweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "seller")
    private List<Item> items = new ArrayList<>();


}
