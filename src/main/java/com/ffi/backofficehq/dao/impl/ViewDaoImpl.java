package com.ffi.backofficehq.dao.impl;

import com.ffi.backofficehq.auth.User;
import com.ffi.backofficehq.dao.ViewDao;
import com.ffi.backofficehq.utils.DynamicRowMapper;
import com.ffi.backofficehq.utils.TableAliasUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
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
        String qry = "Select * from m_staff where photo = '" + token + "' AND outlet_code = '0000' AND ROWNUM = 1";
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

    @Override
    public List<Map<String, Object>> getDetailOutlet(Map<String, Object> params) {
        var query = "SELECT " +
        "mo.AREA_CODE,mo.REGION_CODE,mo.OUTLET_CODE,mo.ADDRESS_1 || ' ' || mo.ADDRESS_2 AS ADDRESS, " +
        "mc.CITY_NAME,mgr.DESCRIPTION AS REGION_NAME,mga.DESCRIPTION AS AREA_NAME,mo.OPEN_TIME, " +
        "mo.TRANS_DATE,mo.CLOSE_TIME,mo.PHONE,mo.FAX,mo.SEND_DATA,mo.IP_OUTLET, mo.PORT_OUTLET,mo.AREA_CODE, " +
        "mo.MONDAY,mo.TUESDAY,mo.WEDNESDAY, mo.THURSDAY,mo.FRIDAY,mo.SATURDAY,mo.SUNDAY " +
        "FROM M_OUTLET mo " +
        "LEFT JOIN M_CITY mc ON mc.CITY_CODE = mo.CITY " +
        "LEFT JOIN M_GLOBAL mgr ON mgr.COND = 'REG_OUTLET' AND mo.REGION_CODE = mgr.CODE " +
        "LEFT JOIN M_GLOBAL mga ON mga.COND = 'AREACODE' AND mo.AREA_CODE = mga.CODE " +
        "WHERE mo.REGION_CODE LIKE '%' || :regionCode || '%' AND mo.OUTLET_CODE LIKE '%' || :outletCode || '%' AND mo.AREA_CODE LIKE '%' || :areaCode || '%'";
         return jdbcTemplate.query(query, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> getDetailGlobal(Map<String, Object> params) {
        var query = "SELECT COND, CODE, DESCRIPTION ,VALUE, STATUS FROM M_GLOBAL mg " +
        "WHERE COND LIKE NVL(:cond, '%') AND CODE LIKE NVL(:code, '%')";
        return jdbcTemplate.query(query, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterTypeOutlet(Map<String, Object> params) {
        String qry = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'OUTLET_TP' AND status = 'A' ORDER BY code";
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterOrderType(Map<String, Object> params) {
        String qry = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'ORDER_TYPE' AND status = 'A' ORDER BY code";
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterRegionOutlet(Map<String, Object> params) {
        String qry = "SELECT code, description FROM M_GLOBAL mg WHERE cond = 'REG_OUTLET' AND status = 'A' ORDER BY code";
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterAreaOutlet(Map<String, Object> params) {
        String qry = """
            SELECT g.code, g.description 
            FROM M_GLOBAL g
            LEFT JOIN m_outlet o ON o.AREA_CODE = g.code
            WHERE g.cond = 'AREACODE' AND g.status = 'A' AND o.REGION_CODE LIKE '%' || '%'
                     """;
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterCondGlobal(Map<String, Object> params) {
        String qry = "SELECT DISTINCT(cond) FROM M_GLOBAL ORDER BY cond";
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

    @Override
    public List<Map<String, Object>> filterOutlet(Map<String, Object> params) {
        String qry = "SELECT mg.code, mg.description, mo.OUTLET_NAME " +
                "FROM M_GLOBAL mg LEFT JOIN M_OUTLET mo ON mo.OUTLET_CODE = mg.CODE " +
                "WHERE MG.COND = 'OUTLET' AND mg.STATUS = 'A' ORDER BY CODE";
        return jdbcTemplate.query(qry, params, new DynamicRowMapper());
    }

}
