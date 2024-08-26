package com.clonecode.orderweb.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String streetAddress;

    public Address(String city, String streetAddress) {
        this.city = city;
        this.streetAddress = streetAddress;
    }

    protected Address() {

    }

    @Override
    public String toString() {
        return city + " " + streetAddress;
    }
}
