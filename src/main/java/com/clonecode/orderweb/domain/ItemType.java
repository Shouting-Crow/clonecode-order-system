package com.clonecode.orderweb.domain;

public enum ItemType {
    SNACK("과자"),
    CLOTHES("의류"),
    ELECTRONICS("전자제품"),
    BOOK("책");

    private final String koreanName;

    ItemType(String koreanName){
        this.koreanName = koreanName;
    }

    public String getKoreanName(){
        return koreanName;
    }
}
