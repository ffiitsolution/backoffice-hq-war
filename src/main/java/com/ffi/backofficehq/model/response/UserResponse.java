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

    private String staffCode;

    private String staffName;

    private String accessLevel;

    private String position;

    private String photo;

    private String status;



}
