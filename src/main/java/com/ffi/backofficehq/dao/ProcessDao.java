package com.ffi.backofficehq.dao;

import com.ffi.backofficehq.model.ApiHqResponse;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author IT
 */
public interface ProcessDao {
    // ========================== NEW Method from M Joko 22-5-2024 ======================
    public ApiHqResponse doLogin(Map<String, Object> params);
//    public Map<String, Object> doLogin(Map<String, Object> params);
}
