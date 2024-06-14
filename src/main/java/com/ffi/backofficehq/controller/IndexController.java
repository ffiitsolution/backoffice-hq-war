package com.ffi.backofficehq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.service.ViewService;
import java.util.HashMap;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class IndexController {
    
    String appVersion = "1.0.2403.14a";

    @Autowired
    private ViewService viewService;

    @RequestMapping(path = "/halo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Map<String, Object>> halo(@RequestBody Map<String, Object> params) {

        Map<String, Object> resp = new HashMap();
        resp.put("version", appVersion);

        return WebResponse.<Map<String, Object>>builder()
                .message("OK")
                .success(true)
                .data(resp)
                .build();
    }

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
