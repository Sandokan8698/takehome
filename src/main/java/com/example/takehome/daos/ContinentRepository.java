package com.example.takehome.daos;

import com.example.takehome.daos.graphql.ContinentsResponse;

import java.util.Collection;

public interface ContinentRepository {
    ContinentsResponse getContinentCountries(Collection<String> continentsCode);
}
