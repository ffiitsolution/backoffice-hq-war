package com.ffi.backofficehq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
//@PropertySource(value = "file:${app.external}")
public class BackofficeHqApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackofficeHqApplication.class, args);
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerImpl();
    }

    private static class WebMvcConfigurerImpl implements WebMvcConfigurer {

        public WebMvcConfigurerImpl() {
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(false);
        }
    }

}
