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
    public ApiHqResponse doLogin(Map<String,Object> params) {
        return dao.doLogin(params);
    }

    public Integer insertMasterGlobal(Map<String,String> params) {
        return dao.insertMasterGlobal(params);
    }

    public Integer updateMasterGlobal(Map<String,String> params) {
        return dao.updateMasterGlobal(params);
    }

    public Integer insertMasterOutlet(Map<String,String> params) {
      return dao.insertOutlet(params);
    }

    public Integer updateOutlet(Map<String,String> params) {
        return dao.updateOutlet(params);
    }



}
