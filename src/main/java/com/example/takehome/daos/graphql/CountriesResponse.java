package com.example.takehome.daos.graphql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class CountriesResponse {

    Data data;

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Continent {
        String code;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Country {
        Continent continent;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Data {
        List<Country> countries;
    }

}
