package com.bsa.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring uses classpath scanning annotations
@Configuration
public class AdminConfiguration {
    /**
     * @Bean is method level annotation
     * The name of the method is actually going to be the name of our bean
     * @Bean can be used when object has complex dependencies resolving or can't be autowired
     */
    @Bean
    public Administrator administrator(AdminCredentialsConfigurationProperties configurationProperties) {
        return new Administrator(
            configurationProperties.getUserName(),
            configurationProperties.getPassword()
        );
    }
}
