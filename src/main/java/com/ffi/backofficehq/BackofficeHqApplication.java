package com.ffi.backofficehq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@PropertySource(value = "file:${app.external}")
public class BackofficeHqApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackofficeHqApplication.class, args);
	}

}
