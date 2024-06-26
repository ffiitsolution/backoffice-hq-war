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
import org.springframework.web.bind.annotation.RestController;

@RestController
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
                SELECT
                	TRANS_DATE,
                	SUM(TOTAL_SALES) AS TOTAL_SALES,
                        COUNT(BILL_NO) AS TOTAL_BILL
                FROM
                	T_POS_BILL tpb
                WHERE
                	DELIVERY_STATUS = 'CLS'
                	AND TRANS_DATE BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')
                GROUP BY
                	TRANS_DATE
                ORDER BY
                	TRANS_DATE
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
