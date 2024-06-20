package com.ffi.backofficehq.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.DataTableResponse;
import com.ffi.backofficehq.service.GlobalService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
public class OutletController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public OutletController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    GlobalService globalService;

    @PostMapping(path = "/api/outlet/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master outlet", description = "List Master Outlet")
    public ResponseEntity<DataTableResponse> dataTable(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_OUTLET";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

}
