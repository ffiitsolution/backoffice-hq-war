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

    public List<Map<String, Object>> listTransMainChart(Map<String, Object> params);

    public List<Map<String, Object>> listMasterDashboardTable(Map<String, Object> params);
}
