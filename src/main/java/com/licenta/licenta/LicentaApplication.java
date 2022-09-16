package com.licenta.licenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class LicentaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicentaApplication.class, args);
	}
}
