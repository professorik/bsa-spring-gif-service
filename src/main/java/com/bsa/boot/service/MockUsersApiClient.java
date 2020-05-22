package com.bsa.boot.service;

import org.springframework.stereotype.Component;

//@Component(value = "mockUserApi") // if we have > 1 same interface binding we must give @Component unique name
public class MockUsersApiClient implements UsersApiClient {
    @Override
    public String getUsersAsJson() {
        return "[{ \"id\": 1, \"name\": \"Bob\" }, { \"id\": 2, \"name\": \"Jane\" }]";
    }
}
