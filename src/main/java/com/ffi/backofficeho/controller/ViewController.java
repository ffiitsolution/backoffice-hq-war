package com.ffi.backofficeho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficeho.model.response.WebResponse;
import com.ffi.backofficeho.service.ViewService;

import java.util.Map;

@RestController
public class ViewController {

    @Autowired
    private ViewService viewService;

    @PostMapping(path = "/api/get-app-profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Map<String, Object>> getAppProfile(@RequestBody Map<String, Object> params) {

        Map<String, Object> profile = viewService.getAppProfile();

        return WebResponse.<Map<String, Object>>builder()
                .message("OK")
                .success(true)
                .data(profile)
                .build();
    }
}
