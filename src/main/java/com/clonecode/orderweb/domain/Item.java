package com.clonecode.orderweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private Long price;
    private Long stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String description;

    @ElementCollection
    private List<String> imageUrls;

    @OneToMany(mappedBy = "item")
    private List<Review> reviews;
}
