package com.ffi.backofficeho.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.ffi.backofficeho.BackofficeHoApplication;

/**
 *
 * @author USER
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BackofficeHoApplication.class);
    }
}