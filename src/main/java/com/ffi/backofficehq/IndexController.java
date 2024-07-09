package com.ffi.backofficehq;

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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/api/master/global/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterGlobalInsert")
    public ResponseEntity<ApiHqResponse> insertGlobalData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            Integer resultData = processServices.insertMasterGlobal(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException dae) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(dae.getMessage());
            System.out.println(getDateTimeForLog() + "insertGlobalData: " + dae.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/global/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterGlobalUpdate")
    public ResponseEntity<ApiHqResponse> updateGlobalData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            Integer resultData = processServices.updateMasterGlobal(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException dae) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(dae.getMessage());
            System.out.println(getDateTimeForLog() + "insertGlobalData: " + dae.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/api/master/global/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletDetail")
    public ResponseEntity<ApiHqResponse> getDetailMasterGlobal(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> data = viewServices.getDetailGlobal(params);
            if (!data.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(data.get(0));
            } else {
                resp.setSuccess(Boolean.FALSE);
                resp.setMessage("No data;");
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println(getDateTimeForLog() + "getDetailMasterGlobal: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletInsert")
    public ResponseEntity<ApiHqResponse> insertOutletData(@RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            Integer resultData = processServices.insertMasterOutlet(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException dae) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(dae.getMessage());
            System.out.println(getDateTimeForLog() + "getDetailOutlet: " + dae.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/api/master/outlet/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletDetail")
    public ResponseEntity<ApiHqResponse> getDetailOutlet(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> data = viewServices.getDetailOutlet(params);
            if (!data.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(data.get(0));
            } else {
                resp.setSuccess(Boolean.FALSE);
                resp.setMessage("No data;");
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println(getDateTimeForLog() + "getDetailOutlet: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OutletDataUpdate")
    public ResponseEntity<ApiHqResponse> updateOutletData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            Integer resultData = processServices.updateOutlet(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException dae) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(dae.getMessage());
            System.out.println(getDateTimeForLog() + "insertGlobalData: " + dae.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
