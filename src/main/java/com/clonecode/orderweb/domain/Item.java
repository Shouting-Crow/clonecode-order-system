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

    private String name;

    private String serialNumber;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private Long price;
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String description;

    private String thumbnailImage;

    @ElementCollection
    @CollectionTable(name = "item_detail_images", joinColumns = @JoinColumn(name = "item_id"))
    private List<String> detailImages;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
