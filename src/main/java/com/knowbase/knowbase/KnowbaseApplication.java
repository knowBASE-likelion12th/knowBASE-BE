package com.knowbase.knowbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//KnowbaseApplication
@SpringBootApplication
@EnableJpaAuditing
public class KnowbaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowbaseApplication.class, args);
	}

}
