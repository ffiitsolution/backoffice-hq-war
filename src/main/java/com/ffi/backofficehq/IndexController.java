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
        map.put("output", "welcome to boffihq");
        map.put("urlDb", urlDb);
        map.put("versionBe", versionBe);
        map.put("versionFe", viewServices.versionFe);
        map.put("warLastUpdate", viewServices.getWarFileLastModified());
        return map;
    }

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " || ";
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

    @PostMapping(path = "/api/master/menu-item/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemLimitAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemLimitUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit-schedule/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitScheduleAdd")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitScheduleAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemLimitScheduleAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-item-limit-schedule/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuItemLimitScheduleUpdate")
    public ResponseEntity<ApiHqResponse> mMenuItemLimitScheduleUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuItemLimitScheduleUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-set/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuSetAdd")
    public ResponseEntity<ApiHqResponse> mMenuSetAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuSetAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-set/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuSetUpdate")
    public ResponseEntity<ApiHqResponse> mMenuSetUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuSetUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-item/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierItemAdd")
    public ResponseEntity<ApiHqResponse> mModifierItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mModifierItemAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierItemUpdate")
    public ResponseEntity<ApiHqResponse> mModifierItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mModifierItemUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet-price/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceAdd")
    public ResponseEntity<ApiHqResponse> mOutletPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mOutletPriceAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/outlet-price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceUpdate")
    public ResponseEntity<ApiHqResponse> mOutletPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mOutletPriceUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/price/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mOutletPriceAdd")
    public ResponseEntity<ApiHqResponse> mPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mPriceAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mPriceUpdate")
    public ResponseEntity<ApiHqResponse> mPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mPriceUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-price/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierPriceAdd")
    public ResponseEntity<ApiHqResponse> mModifierPriceAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mModifierPriceAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/modifier-price/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mModifierPriceUpdate")
    public ResponseEntity<ApiHqResponse> mModifierPriceUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mModifierPriceUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/item/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mItemAdd")
    public ResponseEntity<ApiHqResponse> mItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mItemAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mItemUpdate")
    public ResponseEntity<ApiHqResponse> mItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mItemUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-header/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeHeaderAdd")
    public ResponseEntity<ApiHqResponse> mRecipeHeaderAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeHeaderAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-header/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeHeaderUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeHeaderUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeHeaderUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-detail/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeDetailAdd")
    public ResponseEntity<ApiHqResponse> mRecipeDetailAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeDetailAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-detail/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeDetailUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeDetailUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeDetailUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-product/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeProductAdd")
    public ResponseEntity<ApiHqResponse> mRecipeProductAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeProductAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/recipe-product/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mRecipeProductUpdate")
    public ResponseEntity<ApiHqResponse> mRecipeProductUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mRecipeProductUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/group-item/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mGroupItemAdd")
    public ResponseEntity<ApiHqResponse> mGroupItemAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mGroupItemAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/group-item/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mGroupItemUpdate")
    public ResponseEntity<ApiHqResponse> mGroupItemUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mGroupItemUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupAdd")
    public ResponseEntity<ApiHqResponse> mMenuGroupAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuGroupAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupUpdate")
    public ResponseEntity<ApiHqResponse> mMenuGroupUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuGroupUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group-limit/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupLimitAdd")
    public ResponseEntity<ApiHqResponse> mMenuGroupLimitAdd(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuGroupLimitAdd error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(path = "/api/master/menu-group-limit/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "mMenuGroupLimitUpdate")
    public ResponseEntity<ApiHqResponse> mMenuGroupLimitUpdate(User user, @RequestBody Map<String, Object> params) {
        ApiHqResponse resp = new ApiHqResponse();
        try {
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
            System.out.println(getDateTimeForLog() + "mMenuGroupLimitUpdate error: " + e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
