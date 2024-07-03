package com.ffi.backofficehq.auth;

import com.ffi.backofficehq.services.ViewServices;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    @Autowired
    ViewServices viewServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String apiToken = request.getHeader("x-api-token");
        if (apiToken != null) {
            User user = viewServices.userByToken(apiToken);
            if (user != null) {
                request.setAttribute("user", user);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
