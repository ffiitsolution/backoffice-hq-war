package com.ffi.backofficeho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ffi.backofficeho.entity.User;
import com.ffi.backofficeho.model.request.LoginUserRequest;
import com.ffi.backofficeho.model.response.TokenResponse;
import com.ffi.backofficeho.repository.UserRepository;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getKodeUser()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.OK, "Username or Password Wrong"));

        if(request.getKodePassword() == null ? user.getKodePassword() == null : request.getKodePassword().equals(user.getKodePassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.OK, "Username or Password Wrong");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }

    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
