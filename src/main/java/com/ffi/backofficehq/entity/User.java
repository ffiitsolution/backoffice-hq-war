package com.ffi.backofficehq.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "M_STAFF")
public class User {

    @Id
    @Column(name = "STAFF_CODE")
    private String staffCode;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "STAFF_NAME")
    private String staffName;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "ACCESS_LEVEL")
    private String accessLevel;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PHOTO")
    private String photo;

}
