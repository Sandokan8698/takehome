package com.example.takehome.daos;

import com.example.takehome.daos.graphql.ContinentsResponse;
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
class ContinentRepositoryImplTest {

    @Mock
    private GraphQLClient graphQLClient;

    @InjectMocks
    private ContinentRepositoryImpl subject;

    @Test
    public void testGetContinentCountries() {

        // assert
        var expected = new ContinentsResponse();
        final var query = "{  continents(filter: {code: {in: [\"NA\"]}}) { name  countries { code } }}";
        final var continentsCode = List.of("NA");

        Mockito.when(graphQLClient.request(query, ContinentsResponse.class)).thenReturn(expected);

        // act
        var actual = subject.getContinentCountries(continentsCode);

        // assert
        Assertions.assertEquals(expected, actual);

    }

}