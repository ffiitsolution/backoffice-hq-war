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

    public Integer mMenuItemScheduleAdd(Map<String, Object> params);

    public Integer mMenuItemScheduleUpdate(Map<String, Object> params);

    public Integer mMenuSetAdd(Map<String, Object> params);

    public Integer mMenuSetUpdate(Map<String, Object> params);

    public Integer mModifierItemAdd(Map<String, Object> params);

    public Integer mModifierItemUpdate(Map<String, Object> params);

    public Integer mOutletPriceAdd(Map<String, Object> params);

    public Integer mOutletPriceUpdate(Map<String, Object> params);

    public Integer mPriceAdd(Map<String, Object> params);

    public Integer mPriceUpdate(Map<String, Object> params);

    public Integer mModifierPriceAdd(Map<String, Object> params);

    public Integer mModifierPriceUpdate(Map<String, Object> params);

    public Integer mItemAdd(Map<String, Object> params);

    public Integer mItemUpdate(Map<String, Object> params);

    public Integer mRecipeHeaderAdd(Map<String, Object> params);

    public Integer mRecipeHeaderUpdate(Map<String, Object> params);

    public Integer mRecipeDetailAdd(Map<String, Object> params);

    public Integer mRecipeDetailUpdate(Map<String, Object> params);

    public Integer mRecipeProductAdd(Map<String, Object> params);

    public Integer mRecipeProductUpdate(Map<String, Object> params);

    public Integer mGroupItemAdd(Map<String, Object> params);

    public Integer mGroupItemUpdate(Map<String, Object> params);

    public Integer mMenuGroupAdd(Map<String, Object> params);

    public Integer mMenuGroupUpdate(Map<String, Object> params);

    public Integer mMenuGroupLimitAdd(Map<String, Object> params);

    public Integer mMenuGroupLimitUpdate(Map<String, Object> params);

    public Integer mMpcsHeaderAdd(Map<String, Object> params);

    public Integer mMpcsHeaderUpdate(Map<String, Object> params);

    public Integer mMpcsDetailAdd(Map<String, Object> params);

    public Integer mMpcsDetailUpdate(Map<String, Object> params);

    public Integer mSupplierAdd(Map<String, Object> params);

    public Integer mSupplierUpdate(Map<String, Object> params);

    public Integer mItemSupplierAdd(Map<String, Object> params);

    public Integer mItemSupplierUpdate(Map<String, Object> params);

    public Integer mSyncUpdateAdd(Map<String, Object> params);

    public Integer mSyncUpdateUpdate(Map<String, Object> params);
}
