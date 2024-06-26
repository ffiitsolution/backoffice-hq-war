package com.ffi.backofficehq.controller.master;

import com.ffi.backofficehq.entity.User;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.DataTableResponse;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.service.GlobalService;
import com.ffi.backofficehq.util.DynamicRowMapper;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
public class OutletController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public OutletController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path = "/api/outlet/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master outlet", description = "List Master Outlet")
    public ResponseEntity<DataTableResponse> dataTable(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_OUTLET WHERE STATUS LIKE '%' || :status || '%' AND REGION_CODE LIKE '%' || :regionCode || '%' AND TYPE LIKE '%' || :type || '%' AND AREA_CODE LIKE '%' || :areaCode || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/outlet/param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master outlet", description = "List Master Outlet")
    public ResponseEntity<WebResponse> menuParamCondition(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String queryType = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'OUTLET_TP' AND status = 'A' ORDER BY code";
            List<Map<String, Object>> listType = jdbcTemplate.query(queryType, params, new DynamicRowMapper());

            String queryArea = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'AREACODE' AND status = 'A' ORDER BY code";
            List<Map<String, Object>> listArea = jdbcTemplate.query(queryArea, params, new DynamicRowMapper());

            String queryRegion = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'REG_OUTLET' AND status = 'A' ORDER BY code";
            List<Map<String, Object>> listRegion = jdbcTemplate.query(queryRegion, params, new DynamicRowMapper());

            Map<String, Object> data = Map.of(
                    "listType", listType,
                    "listArea", listArea,
                    "listRegion", listRegion
            );
            resp.setSuccess(Boolean.TRUE);
            resp.setMessage("OK");
            resp.setData(data);

        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("menuParamCondition: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

}
