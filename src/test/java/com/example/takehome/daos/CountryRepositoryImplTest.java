package com.example.takehome.daos;

import com.example.takehome.daos.graphql.CountriesResponse;
import com.example.takehome.daos.graphql.GraphQLClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CountryRepositoryImplTest {

    @Mock
    private GraphQLClient graphQLClient;

    @InjectMocks
    private CountryRepositoryImpl subject;

    @Test
    public void testGetContinentCountries() {

        // assert
        var expected = new CountriesResponse();
        final var query = "{  countries(filter: {code: {in: [\"CA\"]}}) { continent { code } }}";
        final var continentsCode = List.of("CA");

        Mockito.when(graphQLClient.request(query, CountriesResponse.class)).thenReturn(expected);

        // act
        var actual = subject.getCountriesContinent(continentsCode);

        // assert
        Assertions.assertEquals(expected, actual);

    }

}