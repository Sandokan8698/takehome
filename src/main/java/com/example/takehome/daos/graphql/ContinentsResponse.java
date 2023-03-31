package com.example.takehome.daos.graphql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class ContinentsResponse {

    Data data;

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Continent {
        String name;
        List<Country> countries;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Country {
        String code;
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class Data {
        List<Continent> continents;
    }

}
