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

    public Integer mOutletPriceAdd(Map<String, Object> params) {
        return dao.mOutletPriceAdd(params);
    }

    public Integer mOutletPriceUpdate(Map<String, Object> params) {
        return dao.mOutletPriceUpdate(params);
    }

    public Integer mPriceAdd(Map<String, Object> params) {
        return dao.mPriceAdd(params);
    }

    public Integer mPriceUpdate(Map<String, Object> params) {
        return dao.mPriceUpdate(params);
    }

    public Integer mModifierPriceAdd(Map<String, Object> params) {
        return dao.mModifierPriceAdd(params);
    }

    public Integer mModifierPriceUpdate(Map<String, Object> params) {
        return dao.mModifierPriceUpdate(params);
    }

    public Integer mItemAdd(Map<String, Object> params) {
        return dao.mItemAdd(params);
    }

    public Integer mItemUpdate(Map<String, Object> params) {
        return dao.mItemUpdate(params);
    }

    public Integer mRecipeHeaderAdd(Map<String, Object> params) {
        return dao.mRecipeHeaderAdd(params);
    }

    public Integer mRecipeHeaderUpdate(Map<String, Object> params) {
        return dao.mRecipeHeaderUpdate(params);
    }

    public Integer mRecipeDetailAdd(Map<String, Object> params) {
        return dao.mRecipeDetailAdd(params);
    }

    public Integer mRecipeDetailUpdate(Map<String, Object> params) {
        return dao.mRecipeDetailUpdate(params);
    }

    public Integer mRecipeProductAdd(Map<String, Object> params) {
        return dao.mRecipeProductAdd(params);
    }

    public Integer mRecipeProductUpdate(Map<String, Object> params) {
        return dao.mRecipeProductUpdate(params);
    }

    public Integer mGroupItemAdd(Map<String, Object> params) {
        return dao.mGroupItemAdd(params);
    }

    public Integer mGroupItemUpdate(Map<String, Object> params) {
        return dao.mGroupItemUpdate(params);
    }

    public Integer mMenuGroupAdd(Map<String, Object> params) {
        return dao.mMenuGroupAdd(params);
    }

    public Integer mMenuGroupUpdate(Map<String, Object> params) {
        return dao.mMenuGroupUpdate(params);
    }

    public Integer mMenuGroupLimitAdd(Map<String, Object> params) {
        return dao.mMenuGroupLimitAdd(params);
    }

    public Integer mMenuGroupLimitUpdate(Map<String, Object> params) {
        return dao.mMenuGroupLimitUpdate(params);
    }

    public Integer mMpcsHeaderAdd(Map<String, Object> params) {
        return dao.mMpcsHeaderAdd(params);
    }

    public Integer mMpcsHeaderUpdate(Map<String, Object> params) {
        return dao.mMpcsHeaderUpdate(params);
    }

    public Integer mMpcsDetailAdd(Map<String, Object> params) {
        return dao.mMpcsDetailAdd(params);
    }

    public Integer mMpcsDetailUpdate(Map<String, Object> params) {
        return dao.mMpcsDetailUpdate(params);
    }

    public Integer mSupplierAdd(Map<String, Object> params) {
        return dao.mSupplierAdd(params);
    }

    public Integer mSupplierUpdate(Map<String, Object> params) {
        return dao.mSupplierUpdate(params);
    }

    public Integer mItemSupplierAdd(Map<String, Object> params) {
        return dao.mItemSupplierAdd(params);
    }

    public Integer mItemSupplierUpdate(Map<String, Object> params) {
        return dao.mItemSupplierUpdate(params);
    }

    public Integer mSyncUpdateAdd(Map<String, Object> params) {
        return dao.mSyncUpdateAdd(params);
    }

    public Integer mSyncUpdateUpdate(Map<String, Object> params) {
        return dao.mSyncUpdateUpdate(params);
    }

    public Integer mPaymentMethodLimitAdd(Map<String, Object> params) {
        return dao.mPaymentMethodLimitAdd(params);
    }

    public Integer updateMasterPaymentMethodLimit(Map<String, String> params) {
        return dao.updateMasterPaymentMethodLimit(params);
    }

}
