package com.example.takehome.daos.graphql;


import com.example.takehome.AppProperties;
import com.example.takehome.utils.exception.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GraphQLClientImplTest {

    @Mock
    private HttpClient client;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AppProperties appProperties;

    @Captor
    ArgumentCaptor<HttpRequest> httpRequestCaptor;

    @InjectMocks
    private GraphQLClientImpl subject;

    private final String graphQlServerUrl = "http://test";


    @BeforeEach
    public void before() {
        when(appProperties.getGraphQlServerUrl()).thenReturn(graphQlServerUrl);
    }


    @Test
    public void requestShouldThrowApplicationException() throws IOException, InterruptedException {

        // arrange
        var query = "test";
        when(objectMapper.writeValueAsString(any())).thenReturn("");
        when(client.send(any(), any())).thenThrow(InterruptedException.class);

        //assert
        Assertions.assertThrows(ApplicationException.class, () -> subject.request(query, Void.class));
    }

    @Test
    public void requestShouldThrowApplicationExceptionIfTheResponseIsNotSuccessful() throws IOException, InterruptedException {

        // arrange
        final var query = "test";
        when(objectMapper.writeValueAsString(any())).thenReturn(query);
        when(client.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(new HttpResponseImpl<>(null, HttpStatus.BAD_REQUEST));

        // assert
        Assertions.assertThrows(ApplicationException.class, () -> subject.request(query, Void.class));
    }


    @Test
    public void request() throws IOException, InterruptedException {

        // arrange
        final var expected = new CountriesResponse();
        final var query = "test";
        final var response = "{\"data\":{\"countries\":[{\"continent\":{\"code\":\"NA\"}},{\"continent\":{\"code\":\"NA\"}}]}}";
        when(objectMapper.writeValueAsString(any())).thenReturn(query);
        when(client.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(new HttpResponseImpl<>(response, HttpStatus.OK));
        when(objectMapper.readValue(response, CountriesResponse.class)).thenReturn(expected);


        //act
        var actual = subject.request(query, CountriesResponse.class);

        // assert
        Assertions.assertEquals(expected, actual);
        verify(client).send(httpRequestCaptor.capture(), any());
        var captureResult = httpRequestCaptor.getValue();
        Assertions.assertEquals(captureResult.uri().toString(), graphQlServerUrl);
        Assertions.assertTrue(captureResult.bodyPublisher().isPresent());
    }


    private static class HttpResponseImpl<T> implements HttpResponse<T> {

        private final T body;
        private final HttpStatus httpStatus;

        public HttpResponseImpl(T body, HttpStatus httpStatus) {
            this.body = body;
            this.httpStatus = httpStatus;
        }

        @Override
        public int statusCode() {
            return httpStatus.value();
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<T>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return null;
        }

        @Override
        public T body() {
            return body;
        }

        @Override
        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return null;
        }
    }


}