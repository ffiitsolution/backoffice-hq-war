package com.ffi.backofficehq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.request.LoginUserRequest;
import com.ffi.backofficehq.model.response.TokenResponse;
import com.ffi.backofficehq.repository.UserRepository;

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

        User user = userRepository.findFirstByStaffCode(request.getStaffCode()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.OK, "Username Not Found"));

        if(request.getPassword()== null ? user.getPassword()== null : request.getPassword().equals(user.getPassword())) {
            user.setPhoto(UUID.randomUUID().toString());
//            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .photo(user.getPhoto())
//                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.OK, "Password Wrong");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }

    @Transactional
    public void logout(User user){
        user.setPhoto(null);
//        user.setTokenExpiredAt(null);
//
        userRepository.save(user);
    }
}
