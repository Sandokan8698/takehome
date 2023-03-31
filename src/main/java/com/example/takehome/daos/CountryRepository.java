package com.example.takehome.daos;

import com.example.takehome.daos.graphql.CountriesResponse;

import java.util.Collection;

public interface CountryRepository {
    CountriesResponse getCountriesContinent(Collection<String> countryCodes);
}
