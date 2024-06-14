package com.ffi.backofficehq.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.response.DataTableResponse;

/**
 *
 * @author USER
 */
@RestController
public class DataTableController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataTableController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path = "dt/master-location",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master location", description = "list master location")
    public ResponseEntity<DataTableResponse> dtMasterLocation(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_LOCATION";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-supplier",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master supplier", description = "list master supplier")
    public ResponseEntity<DataTableResponse> dtMasterSupplier(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_SUPPLIER";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-area",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master area", description = "list master area")
    public ResponseEntity<DataTableResponse> dtMasterArea(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_AREA";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-branch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master branch", description = "list master branch")
    public ResponseEntity<DataTableResponse> dtMasterBranch(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_BRANCH";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-region",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master region", description = "list master region")
    public ResponseEntity<DataTableResponse> dtMasterRegion(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_REGION";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-rsc",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master rsc", description = "list master rsc")
    public ResponseEntity<DataTableResponse> dtMasterRsc(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_RSC";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-uom",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master uom", description = "list master uom")
    public ResponseEntity<DataTableResponse> dtMasterUom(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_UOM";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-city",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master city", description = "list master city")
    public ResponseEntity<DataTableResponse> dtMasterCity(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_CITY";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/dt/master-product",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master product", description = "list master product")
    public ResponseEntity<DataTableResponse> dtMasterProduct(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM WMS_PRODUCT";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
    
    @PostMapping(path = "/dt/master-outlet",
    consumes =  MediaType.APPLICATION_JSON_VALUE,
    produces =  MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master outlet", description = "List Master Outlet")
    public ResponseEntity<DataTableResponse> dtMasterOutlet(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_OUTLET";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
}
