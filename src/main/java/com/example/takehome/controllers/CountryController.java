package com.example.takehome.controllers;

import com.example.takehome.dtos.CountriesInTheSameContinentDto;
import com.example.takehome.services.CountryService;
import com.example.takehome.utils.ratelimit.SimpleRateLimit;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    private final SimpleRateLimit simpleRateLimit;

    /**
     * @param countryCodes The list of country codes to use a filter
     * @param apiKey       Emulates some sort of user permissions,
     * @return {@link ResponseEntity<CountriesInTheSameContinentDto>}
     */
    @GetMapping
    public ResponseEntity<CountriesInTheSameContinentDto> getCountriesInTheSameContinent(
            @RequestParam("countryCode") List<String> countryCodes,
            @RequestParam(value = "apiKey", required = false) String apiKey
    ) {
        if (simpleRateLimit.enter(Objects.nonNull(apiKey))) {
            return ResponseEntity.ok(countryService.getCountriesInTheSameContinent(countryCodes));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
