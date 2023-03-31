package com.example.takehome.daos.graphql;

public interface GraphQLClient {
    <T> T request(String query, Class<T> clazz);
}
