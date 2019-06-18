package edu.adhira.adhira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class AdhiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdhiraApplication.class, args);
	}

}
