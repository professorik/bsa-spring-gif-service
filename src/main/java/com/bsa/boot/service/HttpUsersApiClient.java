package com.bsa.boot.service;

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
    @Value("${api.users-url}") // we can reference application.properties values directly with @Value
    private String usersApiUrl; // kebab-case to camelCase automatically

    @Autowired
    private HttpClient client;

    @Override
    public String getUsersAsJson() {
        try {
            var response = client.send(buildGetRequest(), HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());

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
