package com.ffi.backofficehq.services;

import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.dao.ViewDao;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class ViewServices {

    @Autowired
    ViewDao dao;

    // ========================== NEW Method from M Joko 7-3-2024 ======================
    public User userByToken(String token) {
        return dao.userByToken(token);
    }

    
    
    // ========================== FILTER from M Joko 7-3-2024 ======================
    
    public List<Map<String, Object>> filterTypeOutlet(Map<String, Object> params) {
        return dao.filterTypeOutlet(params);
    }
    
    public List<Map<String, Object>> filterRegionOutlet(Map<String, Object> params) {
        return dao.filterRegionOutlet(params);
    }
    
    public List<Map<String, Object>> filterAreaOutlet(Map<String, Object> params) {
        return dao.filterAreaOutlet(params);
    }
    
    public List<Map<String, Object>> filterCondGlobal(Map<String, Object> params) {
        return dao.filterCondGlobal(params);
    }
    
    
    
    // ========================== END FILTER from M Joko 7-3-2024 ======================
    
    public List<Map<String, Object>> listTransMainChart(Map<String, Object> params) {
        return dao.listTransMainChart(params);
    }

    public List<Map<String, Object>> listMasterDashboardTable(Map<String, Object> params) {
        return dao.listMasterDashboardTable(params);
    }

    // ========================== DETAIL DATA from M Joko 7-4-2024 ==========================

    public List<Map<String, Object>> getDetailOutlet(Map<String, Object> params) {
        return dao.getDetailOutlet(params);
    }

    public List<Map<String, Object>> getDetailGlobal(Map<String, Object> params) {
        return dao.getDetailGlobal(params);
    }

}
