package com.ffi.backofficehq.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlobalService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GlobalService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public void insert(Map<String, String> params) {
        String sql = "INSERT INTO HQ.M_GLOBAL "
                + "(COND, CODE, DESCRIPTION, VALUE, STATUS, USER_UPD, DATE_UPD, TIME_UPD) "
                + " VALUES (:cond, :code, :description, :value, :status, :userUpd, :dateUpd, :timeUpd) ";
        params.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        try {
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Map<String, String> params) {
        String sql = "UPDATE HQ.M_GLOBAL "
                + "SET DESCRIPTION  = :description, "
                + "value = :value, "
                + "status = :status, "
                + "USER_UPD  = :userUpd, "
                + "DATE_UPD = :dateUpd, "
                + "TIME_UPD = :timeUpd "
                + "WHERE COND = :cond AND CODE = :code";
        params.put("dateUpd", LocalDateTime.now().format(dateFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));

        try {
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
