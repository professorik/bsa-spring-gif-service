package com.bsa.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // enables us to use @ConfigurationProperties annotation
public class BootApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(BootApplication.class, args);

//		var admin = context.getBean(Administrator.class); // when we use DI somewhere Spring basically do similar stuff
//
//		System.out.println(admin.getUserName());
//		System.out.println(admin.getPassword());
//
//		var httpClient1 = context.getBean(HttpClient.class);
//		var httpClient2 = context.getBean(HttpClient.class);
//
//		System.out.println(httpClient1);
//		System.out.println(httpClient2);
	}

}
