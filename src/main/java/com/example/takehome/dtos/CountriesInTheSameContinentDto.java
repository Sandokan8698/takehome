package com.example.takehome.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@AllArgsConstructor
public class CountriesInTheSameContinentDto {

    List<ContinentDto> continent;

    public CountriesInTheSameContinentDto() {
        this.continent = Collections.emptyList();
    }

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
    public static class ContinentDto {
        List<String> countries;
        String name;
        List<String> otherCountries;
    }

}
