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
    
    public Integer mMenuItemAdd(Map<String, Object> params);
    public Integer mMenuItemUpdate(Map<String, Object> params);
    
    public Integer mMenuItemLimitAdd(Map<String, Object> params);
    public Integer mMenuItemLimitUpdate(Map<String, Object> params);
    
    public Integer mMenuItemLimitScheduleAdd(Map<String, Object> params);
    public Integer mMenuItemLimitScheduleUpdate(Map<String, Object> params);
    
    public Integer mMenuSetAdd(Map<String, Object> params);
    public Integer mMenuSetUpdate(Map<String, Object> params);
    
    public Integer mModifierItemAdd(Map<String, Object> params);
    public Integer mModifierItemUpdate(Map<String, Object> params);
}
