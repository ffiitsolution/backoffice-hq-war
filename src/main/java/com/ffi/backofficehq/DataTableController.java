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
        String query = "SELECT * FROM M_GLOBAL WHERE STATUS LIKE '%' || :status || '%' AND COND LIKE '%' || :cond || '%' "
                + "ORDER BY STATUS ASC ";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/outlet/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master outlet", description = "List Master Outlet")
    public ResponseEntity<DataTableResponse> dtOutlet(@RequestBody Map<String, Object> params) {
        String query = "SELECT mo.REGION_CODE, mgo.DESCRIPTION AS REGION_NAME, mo.OUTLET_CODE, mo.AREA_CODE, "
                + "mga.DESCRIPTION AS AREA_NAME, mo.INITIAL_OUTLET, mo.OUTLET_NAME, mo.TYPE, mgr.DESCRIPTION AS TYPE_STORE, "
                + "mo.STATUS FROM M_OUTLET mo "
                + "LEFT JOIN M_GLOBAL mgo ON mgo.COND = 'OUTLET_TP' AND mo.type  = mgo.CODE "
                + "LEFT JOIN M_GLOBAL mga ON mga.COND = 'AREACODE' AND mo.AREA_CODE = mga.CODE "
                + "LEFT JOIN M_GLOBAL mgr ON mgr.COND  = 'REG_OUTLET' AND mo.REGION_CODE = mgr.CODE "
                + "WHERE mo.REGION_CODE LIKE '%' || :regionCode || '%'  "
                + "AND mo.AREA_CODE LIKE '%' || :areaCode || '%' AND mo.TYPE LIKE '%' || :type || '%' "
                + "AND mo.STATUS LIKE '%' || :status || '%' "
                + "ORDER BY mo.REGION_CODE, mo.AREA_CODE, mo.TYPE ASC ";
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

    @PostMapping(path = "/api/payment-method/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master payment", description = "List Master payment")
    public ResponseEntity<DataTableResponse> dtMPaymentMethod(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_PAYMENT_METHOD WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/payment-method-limit/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master payment", description = "List Master payment")
    public ResponseEntity<DataTableResponse> dtMPaymentMethodLimit(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_PAYMENT_METHOD_LIMIT "
        + " WHERE ORDER_TYPE LIKE '%' || :orderType || '%' "
        + " AND REGION_CODE LIKE '%' || :regionCode || '%'  ";
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

    @PostMapping(path = "/api/menu-item/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuItem", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuItem(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_ITEM WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu-item-limit/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuItemLimit", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuItemLimit(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_ITEM_LIMIT";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu-item-schedule/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuItemSchedule", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuItemSchedule(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_ITEM_SCHEDULE";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu-set/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuSet", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuSet(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_SET WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/modifier-item/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMModifierItem", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMModifierItem(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MODIFIER_ITEM WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/outlet-price/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMOutletPrice", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMOutletPrice(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_OUTLET_PRICE";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/price/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMPrice", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMPrice(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_PRICE";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/modifier-price/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMModifierPrice", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMModifierPrice(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MODIFIER_PRICE";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/item/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMItem", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMItem(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_ITEM";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/recipe-header/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMRecipeHeader", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMRecipeHeader(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_RECIPE_HEADER WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/recipe-detail/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMRecipeDetail", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMRecipeDetail(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_RECIPE_DETAIL";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/recipe-product/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMRecipeProduct", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMRecipeProduct(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_RECIPE_PRODUCT";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/group-item/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMGroupItem", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMGroupItem(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_GROUP_ITEM WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu-group/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuGroup", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuGroup(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_GROUP WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/menu-group-limit/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMenuGroup", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMenuGroupLimit(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MENU_GROUP_LIMIT";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/mpcs-header/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMpcsHeader", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMpcsHeader(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MPCS_HEADER WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/mpcs-detail/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMMpcsDetail", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMMpcsDetail(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_MPCS_DETAIL WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/supplier/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMSupplier", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMSupplier(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_SUPPLIER WHERE STATUS LIKE '%' || :status || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/item-supplier/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMItemSupplier", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMItemSupplier(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_ITEM_SUPPLIER WHERE STATUS LIKE '%' || :status || '%' AND  ITEM_CODE LIKE '%' || :itemCode || '%' AND CD_SUPPLIER LIKE '%' || :cdSupplier || '%'";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }

    @PostMapping(path = "/api/sync-update/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "dtMSyncUpdate", description = "DataTable")
    public ResponseEntity<DataTableResponse> dtMSyncUpdate(@RequestBody Map<String, Object> params) {
        String query = "SELECT * FROM M_SYNC_UPDATE WHERE STATUS LIKE '%' || :status || '%' AND DATE_CRT BETWEEN :startDate AND :endDate";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
}
