package org.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient {
    private String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response get(String endpoint) {
        return RestAssured.given().baseUri(baseUrl).get(endpoint);
    }

    // Add other HTTP methods as needed
}

