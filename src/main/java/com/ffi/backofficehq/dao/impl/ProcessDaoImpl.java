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
        var insertQuery = "INSERT INTO M_GLOBAL (COND, CODE, DESCRIPTION, VALUE, STATUS, USER_UPD, DATE_UPD, TIME_UPD) "
                + "VALUES (:cond, :code, :description, :value, :status, :userUpd, :dateUpd, :timeUpd)";
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
        var insertQuery = "INSERT INTO M_OUTLET (REGION_CODE,OUTLET_CODE,OUTLET_NAME,TYPE,ADDRESS_1,ADDRESS_2,CITY,POST_CODE,PHONE,FAX,CASH_BALANCE,TRANS_DATE,DEL_LIMIT,DEL_CHARGE,"
                + "RND_PRINT,RND_FACT,RND_LIMIT,TAX,DP_MIN,CANCEL_FEE,CAT_ITEMS,MAX_BILLS,MIN_ITEMS,REF_TIME,TIME_OUT,MAX_SHIFT,SEND_DATA,MIN_PULL_TRX,MAX_PULL_VALUE,"
                + "STATUS,START_DATE,FINISH_DATE,MAX_DISC_PERCENT,MAX_DISC_AMOUNT,OPEN_TIME,CLOSE_TIME,REFUND_TIME_LIMIT,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY,HOLIDAY,"
                + "OUTLET_24_HOUR,IP_OUTLET,PORT_OUTLET,USER_UPD,DATE_UPD,TIME_UPD,FTP_ADDR,FTP_USER,FTP_PASSWORD,INITIAL_OUTLET,AREA_CODE,RSC_CODE,TAX_CHARGE)"
                + "VALUES (:regionCode,:outletCode,:outletName,:type,:address,:address1,:address2,:city,:postCode,:phone,:fax,:cashBalance,:transDate,:delLimit,:delCharge,"
                + ":rndPrint,:rndFact,:rndLimit,:tax,:dpMin,:cancelFee,:catItem,:maxBills,:minItems,:refTime,:timeOut,:maxShift,:sendData,:minPullTrx,:maxPullValue,:status,:startDate,"
                + ":finishDate,:maxDiscPercent,:maxDiscAmount,:openTime,:closeTime,:refundTimeLimit,:monday,:tuesday,:wednesday,:thursday,:friday,:saturday,:sunday,:holiday,"
                + ":outlet24Hour,:ipOutlet,:portOutlet,:userUpd,:dateUpd,:timeUpd,:ftpAddr,:ftpUser,:ftpPassword,:initialOutlet,:areaCode,:rscCode,:taxCharge";
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(insertQuery, params);
    }

    @Override
    public Integer updateOutlet(Map<String, String> params) {
        StringBuilder updateQuery = new StringBuilder("UPDATE M_OUTLET SET ");
        this.appendUpdateParams(updateQuery, params, "outletName");
        this.appendUpdateParams(updateQuery, params, "type");
        this.appendUpdateParams(updateQuery, params, "address_1");
        this.appendUpdateParams(updateQuery, params, "address_2");
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

        this.appendUpdateTime(updateQuery, params);
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

    @Override
    public Integer mMenuItemAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_ITEM (REGION_CODE, OUTLET_CODE, MENU_ITEM_CODE, MENU_GROUP_CODE, PLU, EI_FLAG, TA_FLAG, CAT_FLAG, BRD_FLAG, SEQ, COLOR_CODE, MANAGER_APPROVAL, DISCOUNTABLE, TAXABLE, MENU_SET, MODIFIER_GROUP1_CODE, MODIFIER_GROUP1_MIN_QTY, MODIFIER_GROUP1_MAX_QTY, MODIFIER_GROUP2_CODE, MODIFIER_GROUP2_MIN_QTY, MODIFIER_GROUP2_MAX_QTY, MODIFIER_GROUP3_CODE, MODIFIER_GROUP3_MIN_QTY, MODIFIER_GROUP3_MAX_QTY, MODIFIER_GROUP4_CODE, MODIFIER_GROUP4_MIN_QTY, MODIFIER_GROUP4_MAX_QTY, MODIFIER_GROUP5_CODE, MODIFIER_GROUP5_MIN_QTY, MODIFIER_GROUP5_MAX_QTY, MODIFIER_GROUP6_CODE, MODIFIER_GROUP6_MIN_QTY, MODIFIER_GROUP6_MAX_QTY, MODIFIER_GROUP7_CODE, MODIFIER_GROUP7_MIN_QTY, MODIFIER_GROUP7_MAX_QTY, CALL_GROUP_CODE, AUTO_SHOW_MODIFIER, STATUS, USER_UPD, DATE_UPD, TIME_UPD) 
            VALUES(:regionCode, :outletCode, :menuItemCode, :menuGroupCode, :plu, :eiFlag, :taFlag, :catFlag, :brdFlag, :seq, :colorCode, :managerApproval, :discountable, :taxable, :menuSet, :modifierGroup1Code, :modifierGroup1MinQty, :modifierGroup1MaxQty, :modifierGroup2Code, :modifierGroup2MinQty, :modifierGroup2MaxQty, :modifierGroup3Code, :modifierGroup3MinQty, :modifierGroup3MaxQty, :modifierGroup4Code, :modifierGroup4MinQty, :modifierGroup4MaxQty, :modifierGroup5Code, :modifierGroup5MinQty, :modifierGroup5MaxQty, :modifierGroup6Code, :modifierGroup6MinQty, :modifierGroup6MaxQty, :modifierGroup7Code, :modifierGroup7MinQty, :modifierGroup7MaxQty, :callGroupCode, :autoShowModifier, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuItemUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_ITEM
            SET 
                MENU_GROUP_CODE = :menuGroupCode,
                PLU = :plu,
                EI_FLAG = :eiFlag,
                TA_FLAG = :taFlag,
                CAT_FLAG = :catFlag,
                BRD_FLAG = :brdFlag,
                SEQ = :seq,
                COLOR_CODE = :colorCode,
                MANAGER_APPROVAL = :managerApproval,
                DISCOUNTABLE = :discountable,
                TAXABLE = :taxable,
                MENU_SET = :menuSet,
                MODIFIER_GROUP1_CODE = :modifierGroup1Code,
                MODIFIER_GROUP1_MIN_QTY = :modifierGroup1MinQty,
                MODIFIER_GROUP1_MAX_QTY = :modifierGroup1MaxQty,
                MODIFIER_GROUP2_CODE = :modifierGroup2Code,
                MODIFIER_GROUP2_MIN_QTY = :modifierGroup2MinQty,
                MODIFIER_GROUP2_MAX_QTY = :modifierGroup2MaxQty,
                MODIFIER_GROUP3_CODE = :modifierGroup3Code,
                MODIFIER_GROUP3_MIN_QTY = :modifierGroup3MinQty,
                MODIFIER_GROUP3_MAX_QTY = :modifierGroup3MaxQty,
                MODIFIER_GROUP4_CODE = :modifierGroup4Code,
                MODIFIER_GROUP4_MIN_QTY = :modifierGroup4MinQty,
                MODIFIER_GROUP4_MAX_QTY = :modifierGroup4MaxQty,
                MODIFIER_GROUP5_CODE = :modifierGroup5Code,
                MODIFIER_GROUP5_MIN_QTY = :modifierGroup5MinQty,
                MODIFIER_GROUP5_MAX_QTY = :modifierGroup5MaxQty,
                MODIFIER_GROUP6_CODE = :modifierGroup6Code,
                MODIFIER_GROUP6_MIN_QTY = :modifierGroup6MinQty,
                MODIFIER_GROUP6_MAX_QTY = :modifierGroup6MaxQty,
                MODIFIER_GROUP7_CODE = :modifierGroup7Code,
                MODIFIER_GROUP7_MIN_QTY = :modifierGroup7MinQty,
                MODIFIER_GROUP7_MAX_QTY = :modifierGroup7MaxQty,
                CALL_GROUP_CODE = :callGroupCode,
                AUTO_SHOW_MODIFIER = :autoShowModifier,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_ITEM_CODE = :menuItemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuItemLimitAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_ITEM_LIMIT
            (REGION_CODE, OUTLET_CODE, MENU_ITEM_CODE, ORDER_TYPE, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:regionCode, :outletCode, :menuItemCode, :orderType, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuItemLimitUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_ITEM_LIMIT
            SET 
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_ITEM_CODE = :menuItemCode
                AND ORDER_TYPE = :orderType
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuItemScheduleAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_ITEM_SCHEDULE
            (REGION_CODE, OUTLET_CODE, MENU_ITEM_CODE, START_DATE, END_DATE, MONDAY, MONDAY_HOUR_START, MONDAY_HOUR_END, TUESDAY, TUESDAY_HOUR_START, TUESDAY_HOUR_END, WEDNESDAY, WEDNESDAY_HOUR_START, WEDNESDAY_HOUR_END, THURSDAY, THURSDAY_HOUR_START, THURSDAY_HOUR_END, FRIDAY, FRIDAY_HOUR_START, FRIDAY_HOUR_END, SATURDAY, SATURDAY_HOUR_START, SATURDAY_HOUR_END, SUNDAY, SUNDAY_HOUR_START, SUNDAY_HOUR_END, HOLIDAY, HOLIDAY_HOUR_START, HOLIDAY_HOUR_END, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:regionCode, :outletCode, :menuItemCode, :startDate, :endDate, :monday, :mondayHourStart, :mondayHourEnd, 
            :tuesday, :tuesdayHourStart, :tuesdayHourEnd, :wednesday, :wednesdayHourStart, :wednesdayHourEnd, 
            :thursday, :thursdayHourStart, :thursdayHourEnd, :friday, :fridayHourStart, :fridayHourEnd, 
            :saturday, :saturdayHourStart, :saturdayHourEnd, :sunday, :sundayHourStart, :sundayHourEnd, 
            :holiday, :holidayHourStart, :holidayHourEnd, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuItemScheduleUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_ITEM_SCHEDULE
            SET 
                END_DATE = :endDate,
                MONDAY = :monday,
                MONDAY_HOUR_START = :mondayHourStart,
                MONDAY_HOUR_END = :mondayHourEnd,
                TUESDAY = :tuesday,
                TUESDAY_HOUR_START = :tuesdayHourStart,
                TUESDAY_HOUR_END = :tuesdayHourEnd,
                WEDNESDAY = :wednesday,
                WEDNESDAY_HOUR_START = :wednesdayHourStart,
                WEDNESDAY_HOUR_END = :wednesdayHourEnd,
                THURSDAY = :thursday,
                THURSDAY_HOUR_START = :thursdayHourStart,
                THURSDAY_HOUR_END = :thursdayHourEnd,
                FRIDAY = :friday,
                FRIDAY_HOUR_START = :fridayHourStart,
                FRIDAY_HOUR_END = :fridayHourEnd,
                SATURDAY = :saturday,
                SATURDAY_HOUR_START = :saturdayHourStart,
                SATURDAY_HOUR_END = :saturdayHourEnd,
                SUNDAY = :sunday,
                SUNDAY_HOUR_START = :sundayHourStart,
                SUNDAY_HOUR_END = :sundayHourEnd,
                HOLIDAY = :holiday,
                HOLIDAY_HOUR_START = :holidayHourStart,
                HOLIDAY_HOUR_END = :holidayHourEnd,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_ITEM_CODE = :menuItemCode
                AND START_DATE = :startDate
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuSetAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_SET
            (REGION_CODE, OUTLET_CODE, MENU_SET_CODE, MENU_SET_ITEM_CODE, SEQ, QTY, MODIFIER_GROUP_CODE, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:regionCode, :outletCode, :menuSetCode, :menuSetItemCode, :seq, :qty, :modifierGroupCode, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuSetUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_SET
            SET 
                SEQ = :seq,
                QTY = :qty,
                MODIFIER_GROUP_CODE = :modifierGroupCode,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_SET_CODE = :menuSetCode
                AND MENU_SET_ITEM_CODE = :menuSetItemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mModifierItemAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MODIFIER_ITEM
            (REGION_CODE, OUTLET_CODE, MODIFIER_GROUP_CODE, MODIFIER_ITEM_CODE, SEQ, COLOR_CODE, STATUS, CALL_GROUP_CODE, USER_UPD, DATE_UPD, TIME_UPD, TAXABLE)
            VALUES(:regionCode, :outletCode, :modifierGroupCode, :modifierItemCode, :seq, :colorCode, :status, :callGroupCode, :userUpd, :dateUpd, :timeUpd, :taxable)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mModifierItemUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MODIFIER_ITEM
            SET 
                SEQ = :seq,
                COLOR_CODE = :colorCode,
                STATUS = :status,
                CALL_GROUP_CODE = :callGroupCode,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd,
                TAXABLE = :taxable
            WHERE 
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MODIFIER_GROUP_CODE = :modifierGroupCode
                AND MODIFIER_ITEM_CODE = :modifierItemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mOutletPriceAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_OUTLET_PRICE
            (OUTLET_CODE, ORDER_TYPE, PRICE_TYPE_CODE, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:outletCode, :orderType, :priceTypeCode, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mOutletPriceUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_OUTLET_PRICE
            SET 
                PRICE_TYPE_CODE = :priceTypeCode,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                OUTLET_CODE = :outletCode
                AND ORDER_TYPE = :orderType
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mPriceAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_PRICE
            (MENU_ITEM_CODE, PRICE_TYPE_CODE, PRICE, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:menuItemCode, :priceTypeCode, :price, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mPriceUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_PRICE
            SET 
                PRICE = :price,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                MENU_ITEM_CODE = :menuItemCode
                AND PRICE_TYPE_CODE = :priceTypeCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mModifierPriceAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MODIFIER_PRICE
            (MODIFIER_GROUP_CODE, MODIFIER_ITEM_CODE, PRICE_TYPE_CODE, PRICE, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:modifierGroupCode, :modifierItemCode, :priceTypeCode, :price, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mModifierPriceUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MODIFIER_PRICE
            SET 
                PRICE = :price,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                MODIFIER_GROUP_CODE = :modifierGroupCode
                AND MODIFIER_ITEM_CODE = :modifierItemCode
                AND PRICE_TYPE_CODE = :priceTypeCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mItemAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_ITEM
            (ITEM_CODE, CD_BRAND, ITEM_DESCRIPTION, CD_LEVEL_1, CD_LEVEL_2, CD_LEVEL_3, CD_LEVEL_4, AMT_COST, UOM_WAREHOUSE, CONV_WAREHOUSE, UOM_PURCHASE, CONV_STOCK, UOM_STOCK, CD_WAREHOUSE, FLAG_OTHERS, FLAG_MATERIAL, FLAG_HALF_FINISH, FLAG_FINISHED_GOOD, FLAG_OPEN_MARKET, FLAG_TRANSFER_LOC, FLAG_CANVASING, FLAG_STOCK, PLU, CD_SUPPLIER_DEFAULT, MIN_STOCK, MAX_STOCK, QTY_STOCK, CD_MENU_ITEM, CD_ITEM_LEFTOVER, STATUS, USER_UPD, DATE_UPD, TIME_UPD, FLAG_PAKET)
            VALUES(:itemCode, :cdBrand, :itemDescription, :cdLevel1, :cdLevel2, :cdLevel3, :cdLevel4, :amtCost, :uomWarehouse, :convWarehouse, :uomPurchase, :convStock, :uomStock, :cdWarehouse, :flagOthers, :flagMaterial, :flagHalfFinish, :flagFinishedGood, :flagOpenMarket, :flagTransferLoc, :flagCanvasing, :flagStock, :plu, :cdSupplierDefault, :minStock, :maxStock, :qtyStock, :cdMenuItem, :cdItemLeftover, :status, :userUpd, :dateUpd, :timeUpd, :flagPaket)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mItemUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_ITEM
            SET 
                CD_BRAND = :cdBrand,
                ITEM_DESCRIPTION = :itemDescription,
                CD_LEVEL_1 = :cdLevel1,
                CD_LEVEL_2 = :cdLevel2,
                CD_LEVEL_3 = :cdLevel3,
                CD_LEVEL_4 = :cdLevel4,
                AMT_COST = :amtCost,
                UOM_WAREHOUSE = :uomWarehouse,
                CONV_WAREHOUSE = :convWarehouse,
                UOM_PURCHASE = :uomPurchase,
                CONV_STOCK = :convStock,
                UOM_STOCK = :uomStock,
                CD_WAREHOUSE = :cdWarehouse,
                FLAG_OTHERS = :flagOthers,
                FLAG_MATERIAL = :flagMaterial,
                FLAG_HALF_FINISH = :flagHalfFinish,
                FLAG_FINISHED_GOOD = :flagFinishedGood,
                FLAG_OPEN_MARKET = :flagOpenMarket,
                FLAG_TRANSFER_LOC = :flagTransferLoc,
                FLAG_CANVASING = :flagCanvasing,
                FLAG_STOCK = :flagStock,
                PLU = :plu,
                CD_SUPPLIER_DEFAULT = :cdSupplierDefault,
                MIN_STOCK = :minStock,
                MAX_STOCK = :maxStock,
                QTY_STOCK = :qtyStock,
                CD_MENU_ITEM = :cdMenuItem,
                CD_ITEM_LEFTOVER = :cdItemLeftover,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd,
                FLAG_PAKET = :flagPaket
            WHERE
                ITEM_CODE = :itemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeHeaderAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_RECIPE_HEADER
            (RECIPE_CODE, RECIPE_REMARK, STATUS, USER_UPD, DATE_UPD, TIME_UPD, MPCS_GROUP)
            VALUES(:recipeCode, :recipeRemark, :status, :userUpd, :dateUpd, :timeUpd, :mpcsGroup)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeHeaderUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_RECIPE_HEADER
            SET 
                RECIPE_REMARK = :recipeRemark,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd,
                MPCS_GROUP = :mpcsGroup
            WHERE
                RECIPE_CODE = :recipeCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeDetailAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_RECIPE_DETAIL
            (RECIPE_CODE, ITEM_CODE, QTY_PURCHASE, UOM_PURCHASE, QTY_STOCK, UOM_STOCK, REMARK, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:recipeCode, :itemCode, :qtyPurchase, :uomPurchase, :qtyStock, :uomStock, :remark, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeDetailUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_RECIPE_DETAIL
            SET 
                QTY_PURCHASE = :qtyPurchase,
                UOM_PURCHASE = :uomPurchase,
                QTY_STOCK = :qtyStock,
                UOM_STOCK = :uomStock,
                REMARK = :remark,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                RECIPE_CODE = :recipeCode
                AND ITEM_CODE = :itemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeProductAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_RECIPE_PRODUCT
            (RECIPE_CODE, PRODUCT_CODE, QTY_PURCHASE, UOM_PURCHASE, QTY_STOCK, UOM_STOCK, PRODUCT_REMARK, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:recipeCode, :productCode, :qtyPurchase, :uomPurchase, :qtyStock, :uomStock, :productRemark, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mRecipeProductUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_RECIPE_PRODUCT
            SET 
                QTY_PURCHASE = :qtyPurchase,
                UOM_PURCHASE = :uomPurchase,
                QTY_STOCK = :qtyStock,
                UOM_STOCK = :uomStock,
                PRODUCT_REMARK = :productRemark,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                RECIPE_CODE = :recipeCode
                AND PRODUCT_CODE = :productCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mGroupItemAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_GROUP_ITEM
            (GROUP_ITEM_CODE, ITEM_CODE, ITEM_DESCRIPTION, MULTIPLY, QTY_STOCK, QTY_STOCK_E, QTY_STOCK_T, UOM_STOCK, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:groupItemCode, :itemCode, :itemDescription, :multiply, :qtyStock, :qtyStockE, :qtyStockT, :uomStock, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mGroupItemUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_GROUP_ITEM
            SET 
                ITEM_DESCRIPTION = :itemDescription,
                MULTIPLY = :multiply,
                QTY_STOCK = :qtyStock,
                QTY_STOCK_E = :qtyStockE,
                QTY_STOCK_T = :qtyStockT,
                UOM_STOCK = :uomStock,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                GROUP_ITEM_CODE = :groupItemCode
                AND ITEM_CODE = :itemCode
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuGroupAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_GROUP
            (REGION_CODE, OUTLET_CODE, MENU_GROUP_CODE, SEQ, COLOR_CODE, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:regionCode, :outletCode, :menuGroupCode, :seq, :colorCode, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuGroupUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_GROUP
            SET 
                SEQ = :seq,
                COLOR_CODE = :colorCode,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_GROUP_CODE = :menuGroupCode;
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuGroupLimitAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MENU_GROUP_LIMIT
            (REGION_CODE, OUTLET_CODE, MENU_GROUP_CODE, ORDER_TYPE, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:regionCode, :outletCode, :menuGroupCode, :orderType, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMenuGroupLimitUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MENU_GROUP_LIMIT
            SET 
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE
                REGION_CODE = :regionCode
                AND OUTLET_CODE = :outletCode
                AND MENU_GROUP_CODE = :menuGroupCode
                AND ORDER_TYPE = :orderType
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMpcsHeaderAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MPCS_HEADER
            (OUTLET_CODE, MPCS_GROUP, DESCRIPTION, FRYER_TYPE, QTY_CONV, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:outletCode, :mpcsGroup, :description, :fryerType, :qtyConv, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMpcsHeaderUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MPCS_HEADER
            SET 
                DESCRIPTION = :description,
                FRYER_TYPE = :fryerType,
                QTY_CONV = :qtyConv,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                OUTLET_CODE = :outletCode AND 
                MPCS_GROUP = :mpcsGroup
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMpcsDetailAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_MPCS_DETAIL
            (OUTLET_CODE, FRYER_TYPE, FRYER_TYPE_SEQ, FRYER_TYPE_CNT, FRYER_TYPE_RESET, STATUS, USER_UPD, DATE_UPD, TIME_UPD, FRYER_TYPE_SEQ_CNT)
            VALUES(:outletCode, :fryerType, :fryerTypeSeq, :fryerTypeCnt, :fryerTypeReset, :status, :userUpd, :dateUpd, :timeUpd, :fryerTypeSeqCnt)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mMpcsDetailUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_MPCS_DETAIL
            SET 
                FRYER_TYPE_CNT = :fryerTypeCnt,
                FRYER_TYPE_RESET = :fryerTypeReset,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd,
                FRYER_TYPE_SEQ_CNT = :fryerTypeSeqCnt
            WHERE 
                OUTLET_CODE = :outletCode AND 
                FRYER_TYPE = :fryerType AND 
                FRYER_TYPE_SEQ = :fryerTypeSeq
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mSupplierAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_SUPPLIER
            (CD_SUPPLIER, SUPPLIER_NAME, ADDRESS_1, ADDRESS_2, CITY, ZIP_CODE, PHONE, FAX, HOMEPAGE, CP_NAME, CP_TITLE, CP_PHONE, CP_PHONE_EXT, CP_MOBILE, CP_EMAIL, FLAG_CANVASING, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:cdSupplier, :supplierName, :address1, :address2, :city, :zipCode, :phone, :fax, :homepage, :cpName, :cpTitle, :cpPhone, :cpPhoneExt, :cpMobile, :cpEmail, :flagCanvasing, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mSupplierUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_SUPPLIER
            SET 
                SUPPLIER_NAME = :supplierName,
                ADDRESS_1 = :address1,
                ADDRESS_2 = :address2,
                CITY = :city,
                ZIP_CODE = :zipCode,
                PHONE = :phone,
                FAX = :fax,
                HOMEPAGE = :homepage,
                CP_NAME = :cpName,
                CP_TITLE = :cpTitle,
                CP_PHONE = :cpPhone,
                CP_PHONE_EXT = :cpPhoneExt,
                CP_MOBILE = :cpMobile,
                CP_EMAIL = :cpEmail,
                FLAG_CANVASING = :flagCanvasing,
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                CD_SUPPLIER = :cdSupplier
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mItemSupplierAdd(Map<String, Object> params) {
        var query = """
            INSERT INTO M_ITEM_SUPPLIER
            (ITEM_CODE, CD_SUPPLIER, STATUS, USER_UPD, DATE_UPD, TIME_UPD)
            VALUES(:itemCode, :cdSupplier, :status, :userUpd, :dateUpd, :timeUpd)
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }

    @Override
    public Integer mItemSupplierUpdate(Map<String, Object> params) {
        var query = """
            UPDATE M_ITEM_SUPPLIER
            SET 
                STATUS = :status,
                USER_UPD = :userUpd,
                DATE_UPD = :dateUpd,
                TIME_UPD = :timeUpd
            WHERE 
                ITEM_CODE = :itemCode AND 
                CD_SUPPLIER = :cdSupplier
                    """;
        params.put("dateUpd", LocalDateTime.now().format(dateTimeOracleFormatter));
        params.put("timeUpd", LocalDateTime.now().format(timeFormatter));
        return jdbcTemplate.update(query, params);
    }
}
