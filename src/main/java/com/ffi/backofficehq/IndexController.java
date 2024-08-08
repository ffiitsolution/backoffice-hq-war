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
import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void init() {
        viewServices.versionBe = versionBe;
    }

    @RequestMapping(value = "/halo")
    public @ResponseBody
    Map<String, Object> tes() {
        Map<String, Object> map = new HashMap<>();
        map.put("output", "welcome to boffihq");
        map.put("urlDb", urlDb);
        map.put("versionBe", viewServices.versionBe);
        map.put("versionFe", viewServices.versionFe);
        map.put("jarLastUpdate", viewServices.getApplicationFileLastModified());
        return map;
    }

    private void printLogOut(String message) {
        System.out.println(LocalDateTime.now().format(dateTimeFormatter) + " || " + message);
    }

    @PostMapping(path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiHqResponse login(@RequestBody Map<String, Object> params) {
        viewServices.versionFe = params.getOrDefault("versionFe", "").toString();
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
            printLogOut("transMainChart: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
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
            printLogOut("masterDashboardTable: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/global/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterGlobalInsert")
    public ResponseEntity<ApiHqResponse> insertGlobalData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("insertGlobalData: " + dae.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/global/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterGlobalUpdate")
    public ResponseEntity<ApiHqResponse> updateGlobalData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("insertGlobalData: " + dae.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/api/master/global/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletDetail")
    public ResponseEntity<ApiHqResponse> getDetailMasterGlobal(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("getDetailMasterGlobal: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletInsert")
    public ResponseEntity<ApiHqResponse> insertOutletData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("getDetailOutlet: " + dae.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping(path = "/api/master/outlet/detail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "masterOutletDetail")
    public ResponseEntity<ApiHqResponse> getDetailOutlet(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("getDetailOutlet: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OutletDataUpdate")
    public ResponseEntity<ApiHqResponse> updateOutletData(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
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
            printLogOut("insertGlobalData: " + dae.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemLimitAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemLimitAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemLimitUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemLimitUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit-schedule/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitScheduleAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitScheduleAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemScheduleAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemLimitScheduleAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit-schedule/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitScheduleUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitScheduleUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuItemScheduleUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuItemLimitScheduleUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-set/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuSetAdd")
    public ResponseEntity<ApiHqResponse> mMenuSetAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuSetAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuSetAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-set/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuSetUpdate")
    public ResponseEntity<ApiHqResponse> mMenuSetUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuSetUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuSetUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-item/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierItemAdd")
    public ResponseEntity<ApiHqResponse> mModifierItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mModifierItemAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mModifierItemAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierItemUpdate")
    public ResponseEntity<ApiHqResponse> mModifierItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mModifierItemUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mModifierItemUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet-price/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceAdd")
    public ResponseEntity<ApiHqResponse> mOutletPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mOutletPriceAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mOutletPriceAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet-price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceUpdate")
    public ResponseEntity<ApiHqResponse> mOutletPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mOutletPriceUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mOutletPriceUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/price/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceAdd")
    public ResponseEntity<ApiHqResponse> mPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mPriceAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mPriceAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mPriceUpdate")
    public ResponseEntity<ApiHqResponse> mPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mPriceUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mPriceUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-price/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierPriceAdd")
    public ResponseEntity<ApiHqResponse> mModifierPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mModifierPriceAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mModifierPriceAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierPriceUpdate")
    public ResponseEntity<ApiHqResponse> mModifierPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mModifierPriceUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mModifierPriceUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/item/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mItemAdd")
    public ResponseEntity<ApiHqResponse> mItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mItemAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mItemAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mItemUpdate")
    public ResponseEntity<ApiHqResponse> mItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mItemUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mItemUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-header/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeHeaderAdd")
    public ResponseEntity<ApiHqResponse> mRecipeHeaderAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeHeaderAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeHeaderAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-header/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeHeaderUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeHeaderUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeHeaderUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeHeaderUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-detail/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeDetailAdd")
    public ResponseEntity<ApiHqResponse> mRecipeDetailAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeDetailAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeDetailAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-detail/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeDetailUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeDetailUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeDetailUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeDetailUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-product/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeProductAdd")
    public ResponseEntity<ApiHqResponse> mRecipeProductAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeProductAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeProductAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-product/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeProductUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeProductUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mRecipeProductUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mRecipeProductUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/group-item/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mGroupItemAdd")
    public ResponseEntity<ApiHqResponse> mGroupItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mGroupItemAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mGroupItemAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/group-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mGroupItemUpdate")
    public ResponseEntity<ApiHqResponse> mGroupItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mGroupItemUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mGroupItemUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupAdd")
    public ResponseEntity<ApiHqResponse> mMenuGroupAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuGroupAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuGroupAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupUpdate")
    public ResponseEntity<ApiHqResponse> mMenuGroupUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuGroupUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuGroupUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group-limit/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupLimitAdd")
    public ResponseEntity<ApiHqResponse> mMenuGroupLimitAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuGroupLimitAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuGroupLimitAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group-limit/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupLimitUpdate")
    public ResponseEntity<ApiHqResponse> mMenuGroupLimitUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mMenuGroupLimitUpdate(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mMenuGroupLimitUpdate error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/payment-method-limit/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mPaymentMethodLimitAdd")
    public ResponseEntity<ApiHqResponse> mPaymentMethodLimitAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.mPaymentMethodLimitAdd(params);
            if (resultData > 0) {
                resp.setSuccess(Boolean.TRUE);
            } else {
                resp.setSuccess(Boolean.FALSE);
            }
            resp.setMessage("OK");
            resp.setData(params);
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("mPaymentMethodLimitAdd error: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/payment-method-limit/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "updateMasterPaymentMethodLimit")
    public ResponseEntity<ApiHqResponse> updateMasterPaymentMethodLimit(User user, @RequestBody Map<String, String> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            params.put("userUpd", user.getStaffCode());
            Integer resultData = processServices.updateMasterPaymentMethodLimit(params);
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
            printLogOut("updateMasterPaymentMethodLimit: " + dae.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/monitoring/outlet-monitoring", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "listOutletMonitoring")
    public ResponseEntity<ApiHqResponse> listOutletMonitoring(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
            List<Map<String, Object>> list = viewServices.listOutletMonitoring(params);
            if (!list.isEmpty()) {
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("OK");
                resp.setData(list);
            }
        } catch (DataAccessException e) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage(e.getMessage());
            printLogOut("transMainChart: " + e.getMessage());
            ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }
}
