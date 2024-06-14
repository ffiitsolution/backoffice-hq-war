package com.ffi.backofficeho.entity;

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
@Table(name = "WMS_USER")
public class User {

    @Id
    @Column(name = "KODE_USER")
    private String kodeUser;

    @Column(name = "KODE_PASSWORD")
    private String kodePassword;

    @Column(name = "NAMA_USER")
    private String namaUser;

    @Column(name = "JABATAN")
    private String jabatan;

    @Column(name = "DEFAULT_LOCATION")
    private String defaultLocation;

    @Column(name = "STATUS_AKTIF")
    private String statusAktif;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "TOKEN_EXPIRED_AT")
    private Long tokenExpiredAt;

}
