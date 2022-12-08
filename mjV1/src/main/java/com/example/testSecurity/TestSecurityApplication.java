package com.example.testSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.example.testSecurity.repository")
public class TestSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestSecurityApplication.class, args);
	}

}
