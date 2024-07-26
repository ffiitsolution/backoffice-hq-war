package com.ffi.backofficehq;

import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.model.ApiHqResponse;
import com.ffi.backofficehq.services.ProcessServices;
import com.ffi.backofficehq.services.ViewServices;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USER
 */
@RestController
public class FilterController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ViewServices viewServices;

    @Autowired
    ProcessServices processServices;

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " || ";
    }
    
    @PostMapping(path = "/api/filter/outlet-type", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter outlet type")
    public ResponseEntity<ApiHqResponse> filterOutletType(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterTypeOutlet(params);
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
            System.out.println(getDateTimeForLog() + "filterOutletType: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/filter/order-type", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter outlet type")
    public ResponseEntity<ApiHqResponse> filterTypeOrder(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterOrderType(params);
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
            System.out.println(getDateTimeForLog() + "filterOrderType: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping(path = "/api/filter/outlet-region", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter outlet region")
    public ResponseEntity<ApiHqResponse> filterOutletRegion(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterRegionOutlet(params);
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
            System.out.println(getDateTimeForLog() + "filterOutletRegion: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping(path = "/api/filter/outlet-area", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter outlet area")
    public ResponseEntity<ApiHqResponse> filterOutletArea(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterAreaOutlet(params);
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
            System.out.println(getDateTimeForLog() + "filterOutletArea: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
    
    @PostMapping(path = "/api/filter/global-cond", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter global cond")
    public ResponseEntity<ApiHqResponse> filterGlobalCond(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterCondGlobal(params);
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
            System.out.println(getDateTimeForLog() + "filterGlobalCond: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/filter/outlet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "filter outlet")
    public ResponseEntity<ApiHqResponse> filterOutlet(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.filterOutlet(params);
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
            System.out.println(getDateTimeForLog() + "filterOutlet: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
