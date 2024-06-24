package com.ffi.backofficehq.controller.master;

import com.ffi.backofficehq.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.backofficehq.model.response.DataTableResponse;
import com.ffi.backofficehq.model.response.WebResponse;
import com.ffi.backofficehq.service.GlobalService;
import com.ffi.backofficehq.util.DynamicRowMapper;
import com.ffi.paging.ResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RestController
public class MenuController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MenuController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    GlobalService menuService;

    @PostMapping(path = "/api/menu/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master menu", description = "List Master Global")
    public ResponseEntity<DataTableResponse> dataTable(User user, @RequestBody Map<String, Object> params) {
        String query = "SELECT M.OUTLET_CODE, M.MENU_GROUP_CODE,M.STATUS,M.SEQ, G.DESCRIPTION AS MENU_GROUP FROM M_MENU_GROUP M JOIN M_GLOBAL G ON M.MENU_GROUP_CODE = G.CODE WHERE G.COND = 'GROUP' AND M.OUTLET_CODE LIKE '%' || :outletCode || '%' AND M.STATUS LIKE '%' || :status || '%' ORDER BY STATUS, SEQ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu/detail/menu-group-limit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master menu", description = "List Master Global")
    public ResponseEntity<WebResponse> menuDetailGroupLimit(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = "SELECT DISTINCT MMGL.OUTLET_CODE, MO.OUTLET_NAME  FROM M_MENU_GROUP_LIMIT mmgl LEFT JOIN M_OUTLET mo ON MMGL.OUTLET_CODE = MO.OUTLET_CODE WHERE MENU_GROUP_CODE  = :menuGroupCode AND ORDER_TYPE = :orderType";
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

    @PostMapping(path = "/api/menu/detail/menu-group-order-type", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master menu", description = "List Master Global")
    public ResponseEntity<WebResponse> menuGroupTipeOrder(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = "SELECT DISTINCT MMGL.ORDER_TYPE , MG.DESCRIPTION  FROM M_MENU_GROUP_LIMIT mmgl LEFT JOIN M_GLOBAL mg ON mg.cond = 'ORDER_TYPE' AND MMGL.ORDER_TYPE = MG.CODE WHERE MENU_GROUP_CODE = :menuGroupCode order by MG.description";
            List<Map<String, Object>> list = jdbcTemplate.query(query, params, new DynamicRowMapper());
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            System.out.println("menuGroupTipeOrder: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/menu/param/outlet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Param master menu", description = "List Master Global")
    public ResponseEntity<WebResponse> menuParamCondition(User user, @RequestBody Map<String, Object> params) {
        WebResponse resp = new WebResponse();
        try {
            String query = "SELECT OUTLET_CODE , OUTLET_NAME  FROM M_OUTLET mo ORDER BY OUTLET_CODE";
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

    @PostMapping(path = "/api/menu/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Insert Global", description = "Insert master menu")
    public @ResponseBody
    ResponseMessage insert(@RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        try {
            menuService.insert(balance);
            rm.setSuccess(true);
            rm.setMessage("Insert Success");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Insert Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }

    @PostMapping(path = "/api/menu/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Global", description = "Update master menu")
    public @ResponseBody
    ResponseMessage update(@RequestBody String param)
            throws IOException, Exception {
        Gson gsn = new Gson();
        Map<String, String> balance = gsn.fromJson(param, new TypeToken<Map<String, Object>>() {
        }.getType());

        List<Map<String, Object>> list = new ArrayList<>();
        ResponseMessage rm = new ResponseMessage();
        try {
            menuService.update(balance);
            rm.setSuccess(true);
            rm.setMessage("UpdateSuccess");

        } catch (Exception e) {
            rm.setSuccess(false);
            rm.setMessage("Update Failed : " + e.getMessage());
        }

        rm.setItem(list);

        return rm;
    }
}
