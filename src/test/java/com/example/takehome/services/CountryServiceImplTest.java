package com.example.takehome.services;

import com.example.takehome.daos.ContinentRepository;
import com.example.takehome.daos.CountryRepository;
import com.example.takehome.daos.graphql.ContinentsResponse;
import com.example.takehome.daos.graphql.CountriesResponse;
import com.example.takehome.dtos.CountriesInTheSameContinentDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ContinentRepository continentRepository;

    @InjectMocks
    private CountryServiceImpl subject;


    @Test
    public void getCountriesInTheSameContinentShouldReturnCountriesInTheSameContinentDtoWithEmptyContent() {

        // arrange
        var mockedSubject = Mockito.spy(subject);
        var countryCodes = List.of("CA");
        var expected = new CountriesInTheSameContinentDto();

        Mockito.doReturn(List.of()).when(mockedSubject).getCountryContinentsCodes(countryCodes);

        // act
        var actual = mockedSubject.getCountriesInTheSameContinent(countryCodes);

        // assert
        Assertions.assertThat(expected).isEqualTo(actual);
        Assertions.assertThat(expected.getContinent()).isEmpty();
    }

    @Test
    public void getCountriesInTheSameContinentShouldReturnCountriesInTheSameContinentDtoWithContent() {

        // arrange
        var mockedSubject = Mockito.spy(subject);
        var countryCodes = List.of("CA");
        var continentsCodes = List.of("NA");
        var expected = new CountriesInTheSameContinentDto(
                List.of(new CountriesInTheSameContinentDto.ContinentDto())
        );

        Mockito.doReturn(continentsCodes).when(mockedSubject).getCountryContinentsCodes(countryCodes);
        Mockito.doReturn(expected).when(mockedSubject).getContinentCountries(Mockito.eq(countryCodes), Mockito.anyList());

        // act
        var actual = mockedSubject.getCountriesInTheSameContinent(countryCodes);

        // assert
        Assertions.assertThat(expected).isEqualTo(actual);
        Assertions.assertThat(expected.getContinent()).isNotEmpty();
    }


    @Test
    public void getCountryContinentsCodes() {

        // arrange
        var countryContinentsCode = Map.of("CA", "NA");
        var countryCodes = List.of("CA");

        Mockito.when(countryRepository.getCountriesContinent(countryCodes))
                .thenReturn(getCountryResponse(countryContinentsCode));

        // act
        var actual = subject.getCountryContinentsCodes(countryCodes);

        // assert
        Assertions.assertThat(actual)
                .containsAll(countryContinentsCode.values());

    }

    @Test
    public void getContinentCountries() {

        // arrange
        var countryContinentsCode = Map.of("CA", "NA");
        Mockito.when(continentRepository.getContinentCountries(countryContinentsCode.values()))
                .thenReturn(getContinentResponse(countryContinentsCode));

        // act
        var actual = subject.getContinentCountries(countryContinentsCode.keySet(), countryContinentsCode.values());

        // assert
        Assertions.assertThat(actual.getContinent()).isNotEmpty();
        Assertions.assertThat(actual.getContinent()).allMatch(continentDto ->
                continentDto.getCountries().contains("CA") &&
                continentDto.getOtherCountries().isEmpty() &&
                continentDto.getName().equals("NA"));
    }


    private CountriesResponse getCountryResponse(Map<String, String> countryContinentsCode) {
        var countryData = countryContinentsCode.values()
                .stream()
                .map(s -> new CountriesResponse.Country(new CountriesResponse.Continent(s)))
                .collect(Collectors.toList());
        return new CountriesResponse(new CountriesResponse.Data(countryData));
    }

    private ContinentsResponse getContinentResponse(Map<String, String> countryContinentsCode) {
        var continentData = countryContinentsCode.entrySet()
                .stream()
                .flatMap(e -> Stream.of(new ContinentsResponse.Continent(
                        e.getValue(),
                        List.of(new ContinentsResponse.Country(e.getKey()))
                )))
                .collect(Collectors.toList());
        return new ContinentsResponse(new ContinentsResponse.Data(continentData));
    }
}