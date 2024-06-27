package com.ffi.backofficehq.controller;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.util.DynamicRowMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final NamedParameterJdbcTemplate jdbcTemplateTrans;

    @Autowired
    public DashboardController(NamedParameterJdbcTemplate jdbcTemplate, @Qualifier("jdbcTemplateTrans") NamedParameterJdbcTemplate jdbcTemplateTrans) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateTrans = jdbcTemplateTrans;
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
            List<Map<String, Object>> list = jdbcTemplateTrans.query(query, params, new DynamicRowMapper());
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

    @PostMapping(path = "/api/master/dashboard/main-table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterDashboardTable")
    public ResponseEntity<WebResponse> masterDashboardTable(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = """
                SELECT b.outlet_name, a.OUTLET_CODE, TO_CHAR(a.DATE_UPD, 'YYYY-MM-DD') AS DATE_UPD, a.DESCRIPTION, a.STATUS
                FROM M_SYNC_UPD_MASTER_DTL a
                JOIN M_OUTLET b ON b.outlet_code = a.outlet_code
                           """;
            List<Map<String, Object>> list = jdbcTemplate.query(query, params, new DynamicRowMapper());
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            } else {
                resp.setSuccess(Boolean.FALSE);
                resp.setMessage("No data;");
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("masterDashboardTable: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
