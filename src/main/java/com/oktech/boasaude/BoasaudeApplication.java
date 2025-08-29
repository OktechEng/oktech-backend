package com.oktech.boasaude;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class BoasaudeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoasaudeApplication.class, args);
	}

}
