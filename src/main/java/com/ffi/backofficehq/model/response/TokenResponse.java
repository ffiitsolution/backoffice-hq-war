package com.ffi.backofficehq.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {

    // temporary kolom token menggunakan photo
    private String photo;

//    private Long expiredAt;
}
