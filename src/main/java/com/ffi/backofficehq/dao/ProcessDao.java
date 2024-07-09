package com.ffi.backofficehq.dao;

import com.ffi.backofficehq.model.ApiHqResponse;

import java.util.Map;

/**
 *
 * @author IT
 */
public interface ProcessDao {
    // ========================== NEW Method from M Joko 22-5-2024 ======================
    public ApiHqResponse doLogin(Map<String, Object> params);
//    public Map<String, Object> doLogin(Map<String, Object> params);

    public Integer insertMasterGlobal(Map<String, String> params);
    public Integer updateMasterGlobal(Map<String, String> params);

    public Integer insertOutlet(Map<String, String> params);
    public Integer updateOutlet(Map<String, String> params);
}
