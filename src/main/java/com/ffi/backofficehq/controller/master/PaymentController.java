package com.ffi.backofficehq.controller.master;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.response.DataTableResponse;
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
public class PaymentController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path = "/api/payment/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master payment", description = "List Master payment")
    public ResponseEntity<DataTableResponse> dataTable(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_PAYMENT WHERE PAYMENT_STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
}
