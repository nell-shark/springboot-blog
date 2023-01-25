package com.nellshark.springbootblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
//		System.setProperty("spring.profiles.active", "dev"); // Delete
		SpringApplication.run(Application.class, args);
	}
}
