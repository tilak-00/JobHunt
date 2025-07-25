package com.example.jobhunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling // Enable scheduling in your Spring Boot application
@EntityScan("com.example.jobhunt.entity")
public class JobHuntApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobHuntApplication.class, args);
	}

}
