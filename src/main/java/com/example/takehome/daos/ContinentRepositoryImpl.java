package com.example.takehome.daos;

import com.example.takehome.daos.graphql.ContinentsResponse;
import com.example.takehome.daos.graphql.GraphQLClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ContinentRepositoryImpl implements ContinentRepository {

    private final GraphQLClient graphQLClient;

    @Override
    public ContinentsResponse getContinentCountries(Collection<String> continentsCode) {
        log.info("Executing getContinentCountries, continentsCode: {}", continentsCode);
        var value = continentsCode.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(","));
        var query = String.format("{  continents(filter: {code: {in: [%s]}}) { name  countries { code } }}", value);
        return graphQLClient.request(query, ContinentsResponse.class);
    }
}
