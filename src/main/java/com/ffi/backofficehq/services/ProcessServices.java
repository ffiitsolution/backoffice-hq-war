package com.ffi.backofficehq.services;

import com.ffi.backofficehq.dao.ProcessDao;
import com.ffi.backofficehq.model.ApiHqResponse;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IT
 */
@Service
public class ProcessServices {

    @Autowired
    ProcessDao dao;

    // ========================== NEW Method from M Joko 22-5-2024 ======================
//    public Integer execVmByOctd(String outletCode, String transDate) {
//        return dao.execVmByOctd(outletCode, transDate);
//    }
    public ApiHqResponse doLogin(Map<String, Object> params) {
        return dao.doLogin(params);
    }

    public Integer insertMasterGlobal(Map<String, String> params) {
        return dao.insertMasterGlobal(params);
    }

    public Integer updateMasterGlobal(Map<String, String> params) {
        return dao.updateMasterGlobal(params);
    }

    public Integer insertMasterOutlet(Map<String, String> params) {
        return dao.insertOutlet(params);
    }

    public Integer updateOutlet(Map<String, String> params) {
        return dao.updateOutlet(params);
    }

    public Integer mMenuItemAdd(Map<String, Object> params) {
        return dao.mMenuItemAdd(params);
    }

    public Integer mMenuItemUpdate(Map<String, Object> params) {
        return dao.mMenuItemUpdate(params);
    }

    public Integer mMenuItemLimitAdd(Map<String, Object> params) {
        return dao.mMenuItemLimitAdd(params);
    }

    public Integer mMenuItemLimitUpdate(Map<String, Object> params) {
        return dao.mMenuItemLimitUpdate(params);
    }

    public Integer mMenuItemScheduleAdd(Map<String, Object> params) {
        return dao.mMenuItemScheduleAdd(params);
    }

    public Integer mMenuItemScheduleUpdate(Map<String, Object> params) {
        return dao.mMenuItemScheduleUpdate(params);
    }

    public Integer mMenuSetAdd(Map<String, Object> params) {
        return dao.mMenuSetAdd(params);
    }

    public Integer mMenuSetUpdate(Map<String, Object> params) {
        return dao.mMenuSetUpdate(params);
    }

    public Integer mModifierItemAdd(Map<String, Object> params) {
        return dao.mModifierItemAdd(params);
    }

    public Integer mModifierItemUpdate(Map<String, Object> params) {
        return dao.mModifierItemUpdate(params);
    }

}
