package com.ffi.backofficehq.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String kodeUser;

    private String namaUser;

    private String jabatan;

    private String defaultLocation;

    private String statusAktif;



}
