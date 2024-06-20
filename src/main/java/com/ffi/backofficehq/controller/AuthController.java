package com.ffi.backofficehq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.request.LoginUserRequest;
import com.ffi.backofficehq.model.response.TokenResponse;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.service.AuthService;
import com.ffi.backofficehq.service.ViewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ViewService viewService;

    @PostMapping(path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<Map<String, Object>> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("staffCode", request.getStaffCode());
        params.put("password", request.getPassword());

//        List<Map<String, Object>> locations = viewService.getLocation(params);

        Map<String, Object> user = viewService.getLoginDetail(params);

        Map<String, Object> res = new HashMap<>();
        res.put("token", tokenResponse.getPhoto());
//        res.put("expiredAt", tokenResponse.getExpiredAt());
        res.put("user", user);

        return WebResponse.<Map<String, Object>>builder()
                .message("OK")
                .success(true)
                .data(res)
                .build();
    }

    @DeleteMapping(path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> logout(User user){
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }
}
