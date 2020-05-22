package com.bsa.boot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "admin")
@Setter
@Getter
public class AdminCredentialsConfigurationProperties {
    private String userName;
    private String password;
}
