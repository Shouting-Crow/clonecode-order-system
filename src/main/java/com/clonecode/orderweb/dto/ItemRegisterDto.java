package com.clonecode.orderweb.dto;

import com.clonecode.orderweb.domain.ItemType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRegisterDto {

    private String name;
    private String serialNumber;
    private Long sellerId;
    private Long price;
    private Integer stockQuantity;
    private ItemType itemType;
    private String description;

    private String thumbnailImageUrl;
    private MultipartFile thumbnailImage;

    private List<MultipartFile> detailImages = new ArrayList<>();
    private List<String> detailImageUrls = new ArrayList<>();

    public void addDetailImageUrl(String imageUrl){
        this.detailImageUrls.add(imageUrl);
    }
}
