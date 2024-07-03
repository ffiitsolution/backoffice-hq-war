package com.ffi.backofficehq;

import com.ffi.backofficehq.auth.CurrentUser;
import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.model.ApiHqResponse;
import com.ffi.backofficehq.services.ProcessServices;
import com.ffi.backofficehq.services.ViewServices;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author IT
 */
@RestController
public class IndexController {

    public String versionBe = "24.06.03a";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ViewServices viewServices;

    @Autowired
    ProcessServices processServices;

    @Value("${spring.datasource.url}")
    private String urlDb;

    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<>();
        map.put("output", "welcome to baoffihq");
        map.put("urlDb", urlDb);
        map.put("versionBe", versionBe);
        return map;
    }

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " || ";
    }

    @PostMapping(path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiHqResponse login(@RequestBody Map<String, Object> params) {
        return processServices.doLogin(params);
    }

    @PostMapping(path = "/api/dashboard/main-transaction-chart", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Main Chart Transaksi")
    public ResponseEntity<ApiHqResponse> transMainChart(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.listTransMainChart(params);
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println(getDateTimeForLog() + "transMainChart: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/dashboard/main-table", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterDashboardTable")
    public ResponseEntity<ApiHqResponse> masterDashboardTable(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.listMasterDashboardTable(params);
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
            System.out.println(getDateTimeForLog() + "masterDashboardTable: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
