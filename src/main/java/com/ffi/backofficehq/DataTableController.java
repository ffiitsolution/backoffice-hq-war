package com.ffi.backofficehq;

import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ffi.backofficehq.model.DataTableResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author USER
 */
@RestController
public class DataTableController {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplateTrans;

    @Autowired
    public DataTableController(NamedParameterJdbcTemplate jdbcTemplate, @Qualifier("jdbcTemplateTrans") NamedParameterJdbcTemplate jdbcTemplateTrans) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateTrans = jdbcTemplateTrans;
    }

    @PostMapping(path = "/api/global/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master global", description = "List Master Global")
    public ResponseEntity<DataTableResponse> dtMGlobal(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_GLOBAL WHERE STATUS LIKE '%' || :status || '%' AND COND LIKE '%' || :cond || '%' " +
                "ORDER BY STATUS ASC ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/outlet/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master outlet", description = "List Master Outlet")
    public ResponseEntity<DataTableResponse> dtOutlet(@RequestBody Map<String, Object> params) {
        String query = "SELECT mo.REGION_CODE, mgo.DESCRIPTION AS REGION_NAME, mo.OUTLET_CODE, mo.AREA_CODE, " +
                "mga.DESCRIPTION AS AREA_NAME, mo.INITIAL_OUTLET, mo.OUTLET_NAME, mo.TYPE, mgr.DESCRIPTION AS TYPE_STORE, " +
                "mo.STATUS FROM M_OUTLET mo " +
                "LEFT JOIN M_GLOBAL mgo ON mgo.COND = 'OUTLET_TP' AND mo.type  = mgo.CODE " +
                "LEFT JOIN M_GLOBAL mga ON mga.COND = 'AREACODE' AND mo.AREA_CODE = mga.CODE " +
                "LEFT JOIN M_GLOBAL mgr ON mgr.COND  = 'REG_OUTLET' AND mo.REGION_CODE = mgr.CODE " +
                "WHERE mo.REGION_CODE LIKE '%' || :regionCode || '%'  " +
                "AND mo.AREA_CODE LIKE '%' || :areaCode || '%' AND mo.TYPE LIKE '%' || :type || '%' " +
                "AND mo.STATUS LIKE '%' || :status || '%' " +
                "ORDER BY mo.REGION_CODE, mo.AREA_CODE, mo.TYPE ASC ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master menu", description = "List Master Global")
    public ResponseEntity<DataTableResponse> dtMenu(@RequestBody Map<String, Object> params) {
        String query = "SELECT M.OUTLET_CODE, M.MENU_GROUP_CODE,M.STATUS,M.SEQ, G.DESCRIPTION AS MENU_GROUP FROM M_MENU_GROUP M JOIN M_GLOBAL G ON M.MENU_GROUP_CODE = G.CODE WHERE G.COND = 'GROUP' AND M.OUTLET_CODE LIKE '%' || :outletCode || '%' AND M.STATUS LIKE '%' || :status || '%' ORDER BY STATUS, SEQ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/payment/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master payment", description = "List Master payment")
    public ResponseEntity<DataTableResponse> dtMPayment(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_PAYMENT WHERE PAYMENT_STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/recipe/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master recipe", description = "List Master Recipe")
    public ResponseEntity<DataTableResponse> dtRecipe(@RequestBody Map<String, Object> params) {
        String query = "select distinct rh.recipe_code, rh.recipe_remark, rh.mpcs_group, mh.description, rh.status from m_recipe_header rh join m_mpcs_header mh on mh.mpcs_group = rh.mpcs_group order by rh.status, rh.recipe_code";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/send-master/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master send-master", description = "")
    public ResponseEntity<DataTableResponse> dtSendMaster(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_SYNC_UPDATE msu WHERE STATUS LIKE '%' || :status || '%' AND DATE_UPD BETWEEN :startDate AND :endDate";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
}
