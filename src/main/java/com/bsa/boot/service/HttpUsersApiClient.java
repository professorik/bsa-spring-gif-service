package com.bsa.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component(value = "realUserApi")
public class HttpUsersApiClient implements UsersApiClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpUsersApiClient.class);

    @Value("${api.users-url}") // we can reference application.properties values directly with @Value
    private String usersApiUrl; // kebab-case to camelCase automatically

    @Autowired
    private HttpClient client;

    @Override
    public String getUsersAsJson() {
        try {
            var response = client.send(buildGetRequest(), HttpResponse.BodyHandlers.ofString());

            logger.info("Response body: " + response.body());

            return response.body();
        } catch (IOException | InterruptedException ex) {
            logger.error(ex.getMessage(), ex);

            return "";
        }
    }

    private HttpRequest buildGetRequest() {
        return HttpRequest
            .newBuilder()
            .uri(URI.create(usersApiUrl))
            .GET()
            .build();
    }
}
