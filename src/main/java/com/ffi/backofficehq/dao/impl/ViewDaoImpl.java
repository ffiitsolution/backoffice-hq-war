package com.ffi.backofficehq.dao.impl;

import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.dao.ViewDao;
import com.ffi.backofficehq.utils.DynamicRowMapper;
import com.ffi.backofficehq.utils.TableAliasUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */
@Repository
public class ViewDaoImpl implements ViewDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    DateTimeFormatter secondFormatter = DateTimeFormatter.ofPattern("HHmmss");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateTimeOracleFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    DateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
    DateFormat dfYear = new SimpleDateFormat("yyyy");

    @Autowired
    private TableAliasUtil tableAliasUtil;
    private final NamedParameterJdbcTemplate jdbcTemplateTrans;

    @Autowired
    public ViewDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, @Qualifier("jdbcTemplateTrans") NamedParameterJdbcTemplate jdbcTemplateTrans) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateTrans = jdbcTemplateTrans;
    }

    @Override
    public User userByToken(String token) {
        String qry = "Select * from m_staff where photo = '" + token + "' AND ROWNUM = 1";
        List<Map<String, Object>> list = jdbcTemplate.query(qry, new HashMap(), new DynamicRowMapper());
        User user = new User();
        if (list != null && !list.isEmpty()) {
            Map<String, Object> map = list.get(0);
            user.setAccessLevel(map.getOrDefault("accessLevel", "").toString());
            user.setPassword(map.getOrDefault("password", "").toString());
            user.setPhoto(map.getOrDefault("photo", "").toString());
            user.setPosition(map.getOrDefault("position", "").toString());
            user.setStaffCode(map.getOrDefault("staffCode", "").toString());
            user.setStaffName(map.getOrDefault("staffName", "").toString());
            user.setStatus(map.getOrDefault("status", "").toString());
        }
        return user;
    }

    @Override
    public List<Map<String, Object>> listTransMainChart(Map<String, Object> params) {
        String qry = """
            SELECT
                TRANS_DATE,
                SUM(TOTAL_SALES) AS TOTAL_SALES,
                    COUNT(BILL_NO) AS TOTAL_BILL
            FROM
                T_POS_BILL tpb
            WHERE
                DELIVERY_STATUS = 'CLS'
                AND TRANS_DATE BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')
            GROUP BY
                TRANS_DATE
            ORDER BY
                TRANS_DATE
                     """;
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> listMasterDashboardTable(Map<String, Object> params) {
        String qry = """
            SELECT b.outlet_name, a.OUTLET_CODE, TO_CHAR(a.DATE_UPD, 'YYYY-MM-DD') AS DATE_UPD, a.DESCRIPTION, a.STATUS
            FROM M_SYNC_UPD_MASTER_DTL a
            JOIN M_OUTLET b ON b.outlet_code = a.outlet_code
                     """;
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

}
