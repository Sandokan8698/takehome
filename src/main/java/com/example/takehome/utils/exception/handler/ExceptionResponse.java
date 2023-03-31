package com.example.takehome.utils.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    Date timestamp;
    HttpStatus status;
    String message;

    Map<String, String> data;
}
