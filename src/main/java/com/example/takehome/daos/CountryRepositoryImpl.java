package com.example.takehome.daos;

import com.example.takehome.daos.graphql.CountriesResponse;
import com.example.takehome.daos.graphql.GraphQLClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final GraphQLClient graphQLClient;

    @Override
    public CountriesResponse getCountriesContinent(Collection<String> countryCodes) {
        log.info("Executing getCountriesContinent, countryCodes: {}", countryCodes);
        var value = countryCodes.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(","));
        var query = String.format("{  countries(filter: {code: {in: [%s]}}) { continent { code } }}", value);
        return graphQLClient.request(query, CountriesResponse.class);
    }

}
