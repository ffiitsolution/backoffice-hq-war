/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.backofficehq.controller;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.util.DynamicRowMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author USER
 */
public class DashboardController {
    

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DashboardController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    

    @PostMapping(path = "/api/dashboard/main-transaction-chart", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master menu", description = "List Master Global")
    public ResponseEntity<WebResponse> transMainChart(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = """
                WITH DateRange AS (
                    SELECT DATE '2024-02-01' AS date_start, DATE '2024-03-01' AS date_end
                    UNION ALL
                    SELECT date_start + INTERVAL 1 DAY, date_end
                    FROM DateRange
                    WHERE date_start + INTERVAL 1 DAY <= date_end
                )
                SELECT dr.date_start AS TRANS_DATE, COALESCE(SUM(tpb.TOTAL_SALES), 0) AS TOTAL_SALES
                FROM DateRange dr
                LEFT JOIN T_POS_BILL tpb ON dr.date_start = tpb.TRANS_DATE AND tpb.DELIVERY_STATUS = 'CLS'
                GROUP BY dr.date_start
                ORDER BY dr.date_start;
                           """;
            List<Map<String, Object>> list = jdbcTemplate.query(query, params, new DynamicRowMapper());
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("menuParamCondition: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
