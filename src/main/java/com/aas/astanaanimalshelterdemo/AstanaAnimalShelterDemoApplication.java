package com.aas.astanaanimalshelterdemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition
@EnableScheduling
public class AstanaAnimalShelterDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstanaAnimalShelterDemoApplication.class, args);
	}
}
