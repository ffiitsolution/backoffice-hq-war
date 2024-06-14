package com.ffi.backofficehq.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.ffi.backofficehq.BackofficeHqApplication;

/**
 *
 * @author USER
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BackofficeHqApplication.class);
    }
}