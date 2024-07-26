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
public class ReportController {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ViewServices viewServices;

    @Autowired
    ProcessServices processServices;

    private void printLogOut(String message) {
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " || " + message);
    }

//    @PostMapping(path = "/api/filter/outlet-type", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "filter outlet type")
//    public ResponseEntity<ApiHqResponse> tesReport(User user, @RequestBody Map<String, Object> params) {
//        ApiHqResponse resp = new ApiHqResponse();
//        try {
//            List<Map<String, Object>> list = viewServices.filterTypeOutlet(params);
//            if (!list.isEmpty()) {
//                resp.setSuccess(Boolean.TRUE);
//                resp.setMessage("OK");
//                resp.setData(list);
//            } else {
//                resp.setSuccess(Boolean.FALSE);
//                resp.setMessage("No data;");
//            }
//        } catch (DataAccessException e) {
//            resp.setSuccess(Boolean.FALSE);
//            resp.setMessage(e.getMessage());
//            printLogOut("filterOutletType: " + e.getMessage());
//        }
//        return ResponseEntity.ok(resp);
//    }

}
