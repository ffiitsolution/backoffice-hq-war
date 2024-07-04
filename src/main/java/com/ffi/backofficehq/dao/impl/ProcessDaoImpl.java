package com.ffi.backofficehq.dao.impl;

import com.ffi.backofficehq.model.ApiHqResponse;
import com.ffi.backofficehq.dao.ProcessDao;
import com.ffi.backofficehq.utils.DynamicRowMapper;
import com.ffi.backofficehq.utils.TableAliasUtil;

import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author IT
 */
@Repository
public class ProcessDaoImpl implements ProcessDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplateTrans;

    DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateTimeOracleFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    DateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat dfYear = new SimpleDateFormat("yyyy");

    @Autowired
    private TableAliasUtil tableAliasUtil;

    @Autowired
    public ProcessDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, @Qualifier("jdbcTemplateTrans") NamedParameterJdbcTemplate jdbcTemplateTrans) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateTrans = jdbcTemplateTrans;
    }

    public String getDateTimeForLog() {
        return LocalDateTime.now().format(dateTimeFormatter) + " || ";
    }

    // ========================== NEW Method from M Joko 22-5-2024 ======================
    @Override
    public ApiHqResponse doLogin(Map<String, Object> params) {
        if (params.get("staffCode") == null || params.get("staffCode").toString().isEmpty()) {
            throw new IllegalArgumentException("Staff Code is required");
        }
        if (params.get("password") == null || params.get("password").toString().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        ApiHqResponse resp = new ApiHqResponse();
        String qry = "SELECT * FROM m_staff WHERE staff_code = :staffCode AND ROWNUM = 1";
        List<Map<String, Object>> listUser = jdbcTemplate.query(qry, params, new DynamicRowMapper());
        if (!listUser.isEmpty() && listUser.size() > 1) {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage("Staff code found in multiple outlet!");
        } else if (!listUser.isEmpty() && listUser.get(0) != null) {
            Map<String, Object> user = listUser.get(0);
            if (!user.get("status").equals("A")) {
                // inactive
                resp.setSuccess(Boolean.FALSE);
                resp.setMessage("User inactive.");
                resp.setData(user);
            } else if (user.get("password").equals(params.get("password"))) {
                // success
                String token = "hq_".concat(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12));
                params.put("token", token);
                params.put("outletCode", user.get("outletCode"));
                String qryToken = "UPDATE M_STAFF SET PHOTO=:token WHERE STAFF_CODE=:staffCode AND OUTLET_CODE=:outletCode";
                jdbcTemplate.update(qryToken, params);
                Map r = new HashMap();
                r.put("user", user);
                r.put("token", token);
                resp.setSuccess(Boolean.TRUE);
                resp.setMessage("Login success.");
                resp.setData(r);
            } else {
                resp.setSuccess(Boolean.FALSE);
                resp.setMessage("Wrong password.");
            }
        } else {
            resp.setSuccess(Boolean.FALSE);
            resp.setMessage("Staff Code not found");
        }
        return resp;
    }

    @Override
    public ApiHqResponse insertMasterGlobal(Map<String, Object> params) {
        return null;
    }

    @Override
    public ApiHqResponse updateMasterGlobal(Map<String, Object> params) {
        return null;
    }

    @Override
    public ApiHqResponse insertOutlet(Map<String, Object> params) {
        var queryString = "INSERT INTO m_outlet VALUES (:staffCode, :outletCode)";
        return null;
    }

    @Override
    public ApiHqResponse updateOutlet(Map<String, Object> params) {
        return null;
    }
}
