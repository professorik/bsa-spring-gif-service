package com.bsa.boot.controller;

import com.bsa.boot.service.UsersApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiUserController {
    @Qualifier(value = "realUserApi") // realUserApi || mockUserApi
    @Autowired
    private UsersApiClient usersApiClient;

    @GetMapping("/api/users")
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(usersApiClient.getUsersAsJson());
    }
}
