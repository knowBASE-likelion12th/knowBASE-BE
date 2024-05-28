package com.knowbase.knowbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//KnowbaseApplication
@SpringBootApplication
@EnableJpaAuditing
public class KnowbaseApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	public static void main(String[] args) {
		SpringApplication.run(KnowbaseApplication.class, args);
	}

}
