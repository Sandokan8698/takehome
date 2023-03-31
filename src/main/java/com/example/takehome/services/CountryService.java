package com.example.takehome.services;

import com.example.takehome.dtos.CountriesInTheSameContinentDto;

import java.util.Collection;

public interface CountryService {
    CountriesInTheSameContinentDto getCountriesInTheSameContinent(Collection<String> codes);
}
