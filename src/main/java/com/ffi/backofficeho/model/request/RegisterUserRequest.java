package com.ffi.backofficeho.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserRequest {

    @NotBlank
    @Size(max = 100)
    private String kodeUser;

    @NotBlank
    @Size(max = 100)
    private String kodePassword;

    @NotBlank
    @Size(max = 100)
    private String namaUser;
}
