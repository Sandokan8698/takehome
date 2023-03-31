package com.example.takehome;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class TakeHomeConfiguration {

    @Bean
    public HttpClient callGraphQLService() {
        return HttpClient.newBuilder()
                .build();
    }


    @Bean
    public ObjectMapper objectMapper() {
        // is easier to isolate, for testing porpoise
        return new ObjectMapper();
    }
}
