package com.bsa.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfiguration {
    @Bean // @Bean can be used when object has complex dependencies resolving or can't be autowired
    public Administrator administrator(AdminCredentialsConfigurationProperties configurationProperties) {
        return new Administrator(
            configurationProperties.getUserName(),
            configurationProperties.getPassword()
        );
    }
}
