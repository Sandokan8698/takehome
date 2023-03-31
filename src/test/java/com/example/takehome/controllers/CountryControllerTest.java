package com.example.takehome.controllers;


import com.example.takehome.AppProperties;
import com.example.takehome.dtos.CountriesInTheSameContinentDto;
import com.example.takehome.services.CountryService;
import com.example.takehome.utils.ratelimit.SimpleRateLimiterImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CountryController.class)
@Import({SimpleRateLimiterImpl.class, AppProperties.class})
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private CountryService countryService;


    @Test
    public void getCountriesInTheSameContinentShouldFailWith400IfQueryParamIsEmpty() throws Exception {

        // assert
        this.mockMvc.perform(get("/countries"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getCountriesInTheSameContinentShouldReturnCountriesInTheSameContinentDto() throws Exception {

        // arrange
        final var countryCodes = List.of("CA");
        final var continent = new CountriesInTheSameContinentDto.ContinentDto(
                countryCodes,
                "North America",
                List.of("AG", "AI")
        );

        final var response = new CountriesInTheSameContinentDto(List.of(continent));
        when(countryService.getCountriesInTheSameContinent(countryCodes)).thenReturn(response);

        // assert
        this.mockMvc.perform(get("/countries?countryCode=CA&apiKey=test"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }


    @Test
    public void getCountriesInTheSameContinentShouldApplyRateLimit() {

        // arrange
        final var countryCodes = List.of("CA");
        final var continent = new CountriesInTheSameContinentDto.ContinentDto(
                countryCodes,
                "North America",
                List.of("AG", "AI")
        );

        final var response = new CountriesInTheSameContinentDto(List.of(continent));
        when(countryService.getCountriesInTheSameContinent(countryCodes)).thenReturn(response);


        // act
        var actual = IntStream.range(0, 6)
                .parallel()
                .mapToObj(i -> {
                    try {
                        return this.mockMvc.perform(get("/countries?countryCode=CA"))
                                .andReturn()
                                .getResponse()
                                .getStatus();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.partitioningBy(s -> s == HttpStatus.TOO_MANY_REQUESTS.value()));


        // assert
        Assertions.assertEquals(actual.get(false).size(), 5);
        Assertions.assertEquals(actual.get(true).size(), 1);

    }


}