package com.example.takehome.daos.graphql;

import com.example.takehome.AppProperties;
import com.example.takehome.utils.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers;


@Slf4j
@Component
public class GraphQLClientImpl implements GraphQLClient {

    private final AppProperties appProperties;

    private final HttpClient client;

    private final ObjectMapper objectMapper;

    public GraphQLClientImpl(AppProperties appProperties, HttpClient client, ObjectMapper objectMapper
    ) {
        this.appProperties = appProperties;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T request(String query, Class<T> clazz) {
        log.info("Executing request, query: {}, clazz: {}", query, clazz);

        try {
            var queryWrapper = new Query(query);
            var stringQueryWrapper = objectMapper.writeValueAsString(queryWrapper);
            var request = HttpRequest.newBuilder()
                    .uri(new URI(appProperties.getGraphQlServerUrl()))
                    .POST(BodyPublishers.ofString(stringQueryWrapper))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.OK.value()) {
                return objectMapper.readValue(response.body(), clazz);
            }

            throw new ApplicationException("The request did not return a successful response");

        } catch (Exception e) {
            log.error("Error executing request, message: {}", e.getMessage());
        }

        throw new ApplicationException("Failed to connect to the external server");
    }

    @lombok.Value
    protected static class Query {
        String query;
    }

}
