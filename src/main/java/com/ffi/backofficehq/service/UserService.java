package com.ffi.backofficehq.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.request.RegisterUserRequest;
import com.ffi.backofficehq.model.request.UpdateUserRequest;
import com.ffi.backofficehq.model.response.UserResponse;
import com.ffi.backofficehq.repository.UserRepository;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getStaffCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");

        }
        User user = new User();
        user.setStaffCode(request.getStaffCode());
        user.setPassword(request.getPassword());
        user.setStaffName(request.getStaffName());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse.builder()
                .staffCode(user.getStaffCode())
                .staffName(user.getStaffName())
                .position(user.getPosition())
                .accessLevel(user.getAccessLevel())
                .photo(user.getPhoto())
                .status(user.getStatus()).build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);

        if (Objects.nonNull(request.getStaffName())) {
            user.setStaffName((request.getStaffName()));
        }
        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);

        log.info("USER : {}", user.getStaffName());

        return UserResponse.builder()
                .staffName(user.getStaffName())
                .staffCode(user.getStaffCode())
                .build();
    }
}
