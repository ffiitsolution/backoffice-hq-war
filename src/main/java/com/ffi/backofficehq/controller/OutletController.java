package com.ffi.backofficehq.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.DataTableResponse;
import com.ffi.backofficehq.service.GlobalService;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class OutletController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OutletController(JdbcTemplate jdbcTemplate) {
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
