package com.ffi.backofficehq.controller;

import com.ffi.backofficehq.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.DataTableResponse;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.service.GlobalService;
import com.ffi.backofficehq.util.DynamicRowMapper;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
public class GlobalController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public GlobalController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    GlobalService globalService;

    @PostMapping(path = "/api/global/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master global", description = "List Master Global")
    public ResponseEntity<DataTableResponse> dataTable(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_GLOBAL WHERE STATUS LIKE '%' || :status || '%' AND COND LIKE '%' || :cond || '%' ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/global/param/condition", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master global", description = "List Master Global")
    public ResponseEntity<WebResponse> globalParamCondition(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = "SELECT DISTINCT(cond) FROM M_GLOBAL ORDER BY cond";
            List<Map<String, Object>> list = jdbcTemplate.query(query, params, new DynamicRowMapper());
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("globalParamCondition: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/global/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert Global", description = "Insert master global")
    public @ResponseBody
    ResponseMessage insert(@RequestBody String param)
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
    public @ResponseBody
    ResponseMessage update(@RequestBody String param)
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
