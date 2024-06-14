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
public class GlobalController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GlobalController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    GlobalService globalService;

    @PostMapping(path = "/api/global/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master global", description = "List Master Global")
    public ResponseEntity<DataTableResponse> dataTable(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_GLOBAL";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/global/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert Global", description = "Insert master global")
    public @ResponseBody ResponseMessage insert(@RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        try {
            globalService.insert(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/global/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Global", description = "Update master global")
    public @ResponseBody ResponseMessage update(@RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        try {
            globalService.update(balance);
            rm.setSuccess(true);
            rm.setMessage("UpdateSuccess");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }
}
