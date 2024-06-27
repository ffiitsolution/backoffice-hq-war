package com.ffi.backofficehq.controller.trans;

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
import com.ffi.backofficehq.util.DynamicRowMapper;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
public class SendMasterController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SendMasterController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path = "/api/send-master/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master send-master", description = "")
    public ResponseEntity<DataTableResponse> dataTable(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_SYNC_UPDATE msu WHERE STATUS LIKE '%' || :status || '%' AND DATE_UPD BETWEEN :startDate AND :endDate";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/send-master/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upsert send master", description = "")
    public ResponseEntity<WebResponse> insertSendMaster(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String queryInactive = "UPDATE M_SYNC_UPDATE SET STATUS='I', USER_UPD=:userUpd, TIME_UPD=TO_CHAR(SYSDATE, 'HH24MISS') WHERE DATE_UPD=TO_DATE(:dateUpd, 'YYYY-MM-DD') AND STATUS='A' AND USER_UPD=:userUpd";
            Integer successInactive = jdbcTemplate.update(queryInactive, params);
            params.put("description", successInactive > 0 ? "Upd Version" : "New Version");
            String queryInsert = "INSERT INTO M_SYNC_UPDATE (SYNC_ID, DESCRIPTION, TOTAL_UPD, REMARK, STATUS, DATE_CRT, USER_CRT, TIME_CRT, DATE_UPD, USER_UPD, TIME_UPD, VERSIONS) VALUES(TO_CHAR(SYSDATE, 'YYYYMMDD') || LPAD( (SELECT count(*) FROM M_SYNC_UPDATE WHERE DATE_CRT = TO_CHAR(SYSDATE, 'DD MON YYYY') ) + 1, 2, '0'), :description, :totalUpd, :remark, 'A', TO_CHAR(SYSDATE, 'DD MON YYYY'), :userUpd, TO_CHAR(SYSDATE, 'HH24MISS'), TO_CHAR(TO_DATE(:dateUpd, 'YYYY-MM-DD'), 'DD MON YYYY'), :userUpd, TO_CHAR(SYSDATE, 'HH24MISS'), (TO_CHAR(TO_DATE(:dateUpd, 'YYYY-MM-DD'), 'YYYYMMDD') || '.V.' || LPAD( (SELECT count(*) FROM M_SYNC_UPDATE WHERE DATE_UPD = TO_DATE(:dateUpd, 'YYYY-MM-DD') AND USER_CRT = :userUpd ) + 1, 2, '0') ) )";
            Integer success = jdbcTemplate.update(queryInsert, params);
            resp.setSuccess(success > 0 ? Boolean.TRUE : Boolean.FALSE);
            resp.setMessage("OK");

        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("insertSendMaster error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/send-master/inactive", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upsert send master", description = "")
    public ResponseEntity<WebResponse> inactiveSendMaster(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String queryInactive = "UPDATE M_SYNC_UPDATE SET STATUS='N', USER_UPD=:userUpd, TIME_UPD=TO_CHAR(SYSDATE, 'HH24MISS') WHERE DATE_UPD=TO_DATE(:dateUpd, 'YYYY-MM-DD') AND STATUS='Y' AND USER_UPD=:userUpd AND SYNC_ID = :syncId";
            Integer successInactive = jdbcTemplate.update(queryInactive, params);
            resp.setSuccess(successInactive > 0 ? Boolean.TRUE : Boolean.FALSE);
            resp.setMessage("OK");
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("inactiveSendMaster error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

}
