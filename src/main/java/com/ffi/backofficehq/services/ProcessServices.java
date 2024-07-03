package com.ffi.backofficehq.services;

import com.ffi.backofficehq.dao.ProcessDao;
import com.ffi.backofficehq.model.ApiHqResponse;
import java.util.List;
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
}
