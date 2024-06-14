package com.ffi.backofficeho.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ffi.backofficeho.entity.User;
import com.ffi.backofficeho.model.request.RegisterUserRequest;
import com.ffi.backofficeho.model.request.UpdateUserRequest;
import com.ffi.backofficeho.model.response.UserResponse;
import com.ffi.backofficeho.repository.UserRepository;

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

        if (userRepository.existsById(request.getKodeUser())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");

        }
        User user = new User();
        user.setKodeUser(request.getKodeUser());
        user.setKodePassword(request.getKodePassword());
        user.setNamaUser(request.getNamaUser());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        return UserResponse.builder()
                .kodeUser(user.getKodeUser())
                .namaUser(user.getNamaUser())
                .jabatan(user.getJabatan())
                .defaultLocation(user.getDefaultLocation())
                .statusAktif(user.getStatusAktif()).build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);

        if (Objects.nonNull(request.getNamaUser())) {
            user.setNamaUser((request.getNamaUser()));
        }
        if (Objects.nonNull(request.getKodePassword())) {
            user.setKodePassword(request.getKodePassword());
        }

        userRepository.save(user);

        log.info("USER : {}", user.getNamaUser());

        return UserResponse.builder()
                .namaUser(user.getNamaUser())
                .kodeUser(user.getKodeUser())
                .build();
    }
}
