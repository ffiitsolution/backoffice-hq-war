package com.ffi.backofficehq.services;

import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.dao.ViewDao;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public String versionFe = "";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    ViewDao dao;

    public String getWarFileLastModified() {
        Long lastModified;
        try {
            File warFile = Paths.get(ViewServices.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();
            lastModified = warFile.lastModified();
        } catch (URISyntaxException e) {
            lastModified = -1l;
        }
        if (lastModified != -1) {
            LocalDateTime lastModifiedDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(lastModified), ZoneId.systemDefault());
            return lastModifiedDateTime.format(formatter);
        }
        return "";
    }

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

    public List<Map<String, Object>> filterOutlet(Map<String, Object> params) {
        return dao.filterOutlet(params);
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
