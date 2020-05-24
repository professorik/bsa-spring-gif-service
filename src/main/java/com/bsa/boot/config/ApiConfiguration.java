package com.bsa.boot.config;

import com.bsa.boot.service.HttpUsersApiClient;
import com.bsa.boot.service.MockUsersApiClient;
import com.bsa.boot.service.UsersApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.http.HttpClient; // java 11

@Configuration
public class ApiConfiguration {
    @Bean
    @Scope(value = "prototype") // singleton by default
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

//    @Bean(name = "realUserApi")
//    public UsersApiClient realUsersApiClient(HttpClient client) {
//        return new HttpUsersApiClient(client);
//    }

//    @Bean(name = "mockUserApi")
//    public UsersApiClient mockUsersApiClient() {
//        return new MockUsersApiClient();
//    }
}
