package com.example.takehome.services;

import com.example.takehome.daos.ContinentRepository;
import com.example.takehome.daos.CountryRepository;
import com.example.takehome.daos.graphql.ContinentsResponse;
import com.example.takehome.dtos.CountriesInTheSameContinentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ContinentRepository continentRepository;

    @Override
    public CountriesInTheSameContinentDto getCountriesInTheSameContinent(Collection<String> codes) {
        log.info("Executing getCountriesInTheSameContinent, codes: {}", codes);

        var continentCodes = getCountryContinentsCodes(codes);

        if (continentCodes.isEmpty()) {
            return new CountriesInTheSameContinentDto();
        }

        return getContinentCountries(codes, continentCodes);

    }

    protected List<String> getCountryContinentsCodes(Collection<String> codes) {
        log.info("Executing getCountryContinentsCodes, codes: {}", codes);
        return countryRepository.getCountriesContinent(codes)
                .getData().getCountries()
                .stream()
                .map(country -> country.getContinent().getCode())
                .collect(Collectors.toList());
    }


    protected CountriesInTheSameContinentDto getContinentCountries(
            Collection<String> codes,
            Collection<String> continentCodes
    ) {
        log.info("Executing getContinentCountries, codes: {} continentCodes: {}", codes, continentCodes);
        var continentDtos = continentRepository.getContinentCountries(continentCodes)
                .getData()
                .getContinents()
                .stream()
                .map(continent -> {

                    var otherCountries = continent.getCountries()
                            .stream()
                            .map(ContinentsResponse.Country::getCode)
                            .collect(Collectors.partitioningBy(codes::contains));

                    return new CountriesInTheSameContinentDto.ContinentDto(
                            otherCountries.get(true),
                            continent.getName(),
                            otherCountries.get(false)
                    );

                })
                .collect(Collectors.toList());

        return new CountriesInTheSameContinentDto(continentDtos);
    }
}
