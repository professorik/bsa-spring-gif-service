package com.bsa.boot.config;

import lombok.Getter;

@Getter
public class Administrator {
    private final String userName;
    private final String password;

    public Administrator(String userName, String password) {
        this.userName = userName;
        this.password = password;

        System.out.println("Admin created");
    }
}
