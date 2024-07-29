package com.ffi.backofficehq.dao;

import com.ffi.backofficehq.auth.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public interface ViewDao {

    // ========================== NEW Method from M Joko 3-7-2024 ======================
    public User userByToken(String token);
    
    
    // ========================== FILTER from M Joko 3-7-2024 ======================
    public List<Map<String, Object>> filterTypeOutlet(Map<String, Object> params);
    public List<Map<String, Object>> filterOrderType(Map<String, Object> params);
    public List<Map<String, Object>> filterRegionOutlet(Map<String, Object> params);
    public List<Map<String, Object>> filterAreaOutlet(Map<String, Object> params);
    public List<Map<String, Object>> filterCondGlobal(Map<String, Object> params);
    public List<Map<String, Object>> filterOutlet(Map<String, Object> params);
    // ========================== END FILTER from M Joko 3-7-2024 ======================
    
    // ========================== FILTER PAYMENT METOD & PAYMENT METHOD LIMIT 25-7-2024 & 29-7-2024 ======================
    public List<Map<String, Object>> filterPaymentMethodCode(Map<String, Object> params);
    public List<Map<String, Object>> filterPaymentTypeCode(Map<String, Object> params);

    // ========================== NEW Method from M Joko 3-7-2024 ======================
    public List<Map<String, Object>> listTransMainChart(Map<String, Object> params);

    public List<Map<String, Object>> listMasterDashboardTable(Map<String, Object> params);

    public List<Map<String, Object>> getDetailOutlet(Map<String, Object> params);
    public List<Map<String, Object>> getDetailGlobal(Map<String, Object> params);
}
