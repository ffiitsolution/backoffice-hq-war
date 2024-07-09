package com.ffi.backofficehq.dao.impl;

import com.ffi.backofficehq.model.ApiHqResponse;
import com.ffi.backofficehq.dao.ProcessDao;
import com.ffi.backofficehq.utils.DynamicRowMapper;
import com.ffi.backofficehq.utils.TableAliasUtil;

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

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
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
    public Integer insertMasterGlobal(Map<String, String> params) {
        var insertQuery = "INSERT INTO M_GLOBAL (COND, CODE, DESCRIPTION, VALUE, STATUS, USER_UPD, DATE_UPD, TIME_UPD) " +
                "VALUES (:cond, :code, :description, :value, :status, :userUpd, :dateUpd, :timeUpd)";
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(insertQuery, params);
    }

    @Override
    public Integer updateMasterGlobal(Map<String, String> params) {
        StringBuilder updateQuery = new StringBuilder("UPDATE M_GLOBAL SET ");
        this.appendUpdateParams(updateQuery, params, "cond");
        this.appendUpdateParams(updateQuery, params, "description");
        this.appendUpdateParams(updateQuery, params, "value");
        this.appendUpdateParams(updateQuery, params, "status");
        this.appendUpdateParams(updateQuery, params, "userUpd");

        this.appendUpdateTime(updateQuery, params);
        updateQuery.append(" WHERE CODE = :code");
        return jdbcTemplate.update(String.valueOf(updateQuery), params);
    }

    @Override
    public Integer insertOutlet(Map<String, String> params) {
        var insertQuery = "INSERT INTO M_OUTLET (REGION_CODE,OUTLET_CODE,OUTLET_NAME,TYPE,ADDRESS_1,ADDRESS_2,CITY,POST_CODE,PHONE,FAX,CASH_BALANCE,TRANS_DATE,DEL_LIMIT,DEL_CHARGE," +
                "RND_PRINT,RND_FACT,RND_LIMIT,TAX,DP_MIN,CANCEL_FEE,CAT_ITEMS,MAX_BILLS,MIN_ITEMS,REF_TIME,TIME_OUT,MAX_SHIFT,SEND_DATA,MIN_PULL_TRX,MAX_PULL_VALUE," +
                "STATUS,START_DATE,FINISH_DATE,MAX_DISC_PERCENT,MAX_DISC_AMOUNT,OPEN_TIME,CLOSE_TIME,REFUND_TIME_LIMIT,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY,HOLIDAY," +
                "OUTLET_24_HOUR,IP_OUTLET,PORT_OUTLET,USER_UPD,DATE_UPD,TIME_UPD,FTP_ADDR,FTP_USER,FTP_PASSWORD,INITIAL_OUTLET,AREA_CODE,RSC_CODE,TAX_CHARGE)" +
                "VALUES (:regionCode,:outletCode,:outletName,:type,:address,:address1,:address2,:city,:postCode,:phone,:fax,:cashBalance,:transDate,:delLimit,:delCharge," +
                ":rndPrint,:rndFact,:rndLimit,:tax,:dpMin,:cancelFee,:catItem,:maxBills,:minItems,:refTime,:timeOut,:maxShift,:sendData,:minPullTrx,:maxPullValue,:status,:startDate," +
                ":finishDate,:maxDiscPercent,:maxDiscAmount,:openTime,:closeTime,:refundTimeLimit,:monday,:tuesday,:wednesday,:thursday,:friday,:saturday,:sunday,:holiday," +
                ":outlet24Hour,:ipOutlet,:portOutlet,:userUpd,:dateUpd,:timeUpd,:ftpAddr,:ftpUser,:ftpPassword,:initialOutlet,:areaCode,:rscCode,:taxCharge";
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(insertQuery, params);
    }

    @Override
    public Integer updateOutlet(Map<String, String> params) {
        StringBuilder updateQuery = new StringBuilder("UPDATE M_OUTLET SET ");
        this.appendUpdateParams(updateQuery, params, "outletName");
        this.appendUpdateParams(updateQuery, params, "type");
        this.appendUpdateParams(updateQuery, params, "address1");
        this.appendUpdateParams(updateQuery, params, "address2");
        this.appendUpdateParams(updateQuery, params, "city");
        this.appendUpdateParams(updateQuery, params, "postCode");
        this.appendUpdateParams(updateQuery, params, "phone");
        this.appendUpdateParams(updateQuery, params, "fax");
        this.appendUpdateParams(updateQuery, params, "cashBalance");
        this.appendUpdateParams(updateQuery, params, "transDate");
        this.appendUpdateParams(updateQuery, params, "delLimit");
        this.appendUpdateParams(updateQuery, params, "delCharge");
        this.appendUpdateParams(updateQuery, params, "rndPrint");
        this.appendUpdateParams(updateQuery, params, "rndFact");
        this.appendUpdateParams(updateQuery, params, "rndLimit");
        this.appendUpdateParams(updateQuery, params, "tax");
        this.appendUpdateParams(updateQuery, params, "dpMin");
        this.appendUpdateParams(updateQuery, params, "cancelFee");
        this.appendUpdateParams(updateQuery, params, "catItems");
        this.appendUpdateParams(updateQuery, params, "maxBills");
        this.appendUpdateParams(updateQuery, params, "minItems");
        this.appendUpdateParams(updateQuery, params, "refTime");
        this.appendUpdateParams(updateQuery, params, "timeOut");
        this.appendUpdateParams(updateQuery, params, "maxShift");
        this.appendUpdateParams(updateQuery, params, "sendData");
        this.appendUpdateParams(updateQuery, params, "minPullTrx");
        this.appendUpdateParams(updateQuery, params, "maxPullValue");
        this.appendUpdateParams(updateQuery, params, "status");
        this.appendUpdateParams(updateQuery, params, "startDate");
        this.appendUpdateParams(updateQuery, params, "finishDate");
        this.appendUpdateParams(updateQuery, params, "maxDiscPercent");
        this.appendUpdateParams(updateQuery, params, "maxDiscAmount");
        this.appendUpdateParams(updateQuery, params, "openTime");
        this.appendUpdateParams(updateQuery, params, "closeTime");
        this.appendUpdateParams(updateQuery, params, "refundTimeLimit");
        this.appendUpdateParams(updateQuery, params, "monday");
        this.appendUpdateParams(updateQuery, params, "tuesday");
        this.appendUpdateParams(updateQuery, params, "wednesday");
        this.appendUpdateParams(updateQuery, params, "thursday");
        this.appendUpdateParams(updateQuery, params, "friday");
        this.appendUpdateParams(updateQuery, params, "saturday");
        this.appendUpdateParams(updateQuery, params, "sunday");
        this.appendUpdateParams(updateQuery, params, "holiday");
        this.appendUpdateParams(updateQuery, params, "outlet24Hour");
        this.appendUpdateParams(updateQuery, params, "ipOutlet");
        this.appendUpdateParams(updateQuery, params, "portOutlet");
        this.appendUpdateParams(updateQuery, params, "userUpd");
        this.appendUpdateParams(updateQuery, params, "ftpAddr");
        this.appendUpdateParams(updateQuery, params, "ftpUser");
        this.appendUpdateParams(updateQuery, params, "ftpPassword");
        this.appendUpdateParams(updateQuery, params, "initialOutlet");
        this.appendUpdateParams(updateQuery, params, "areaCode");
        this.appendUpdateParams(updateQuery, params, "rscCode");
        this.appendUpdateParams(updateQuery, params, "taxCharge");

        this.appendUpdateTime(updateQuery,params);
        updateQuery.append(" WHERE REGION_CODE = :regionCode AND OUTLET_CODE = :outletCode");
        return jdbcTemplate.update(String.valueOf(updateQuery), params);
    }

    public void appendUpdateParams(StringBuilder sbl, Map<String, String> params, String keyParam) {
        if (params.containsKey(keyParam)) {
            sbl.append(new DynamicRowMapper().convertToSnakeCase(keyParam)).append(" = :").append(keyParam).append(", ");
        }
    }

    public void appendUpdateTime(StringBuilder sbl, Map<String, String> params) {
        sbl.append("DATE_UPD").append(" = :").append("dateUpd").append(", ");
        sbl.append("TIME_UPD").append(" = :").append("timeUpd");

        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
    }
}
