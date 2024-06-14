package com.ffi.backofficeho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ffi.backofficeho.util.DynamicRowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ViewService {


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ViewService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public String getRsc() {
        String query = "SELECT DEFAULT_RSC FROM WMS_PROFILE WHERE ROWNUM = 1";
        return jdbcTemplate.queryForObject(query, new HashMap<>(), String.class);
    }

    public Map<String,Object> getAppProfile() {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        String query = "SELECT * FROM WMS_PROFILE WHERE ROWNUM = 1";
        result = jdbcTemplate.query(query, new HashMap<>(), new DynamicRowMapper());
        return result.get(0);
    }

    public List<Map<String,Object>> getLocation(Map<String,Object> params) {
        List result = new ArrayList();
        String query = "SELECT * FROM WMS_LOCATION a LEFT JOIN WMS_USERLOC c ON a.KODE_LOCATION  = c.KODE_LOCATION WHERE c.KODE_USER = :kodeUser";
        result = jdbcTemplate.query(query, params, new DynamicRowMapper());
        return result;
    }

    public Map<String,Object> getLoginDetail(Map<String,Object> params) {
        List<Map<String,Object>> result = new ArrayList<>();
        String query = "SELECT KODE_USER, NAMA_USER, JABATAN, DEFAULT_LOCATION,STATUS_AKTIF  FROM WMS_USER WHERE KODE_USER = :kodeUser";
        result = jdbcTemplate.query(query, params, new DynamicRowMapper());
        return result.get(0);
    }

    public Map<String,Object> getOutlet(Map<String,Object> params) {
        List<Map<String,Object>> result = new ArrayList<>();
        String query = "SELECT * FROM M_OUTLET";
        result = jdbcTemplate.query(query, params, new DynamicRowMapper());
        return result.get(0);
    }
}
