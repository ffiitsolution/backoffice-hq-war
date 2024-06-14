package com.ffi.backofficeho.util;

/**
 *
 * @author USER
 *
 * added by M Joko - 20/12/23 dapat digunakan ketika get data dan otomatis
 * mapping hasil return
 *
 * jdbcTemplate.query(sql, paramMap, new DynamicRowMapper());
 *
 */
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DynamicRowMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Map<String, Object> result = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object columnValue = rs.getObject(i);
            String camelCaseColumnName = convertToCamelCase(columnName);
            if (columnValue instanceof Date date) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(date);
                result.put(camelCaseColumnName, formattedDate);
            } else if (columnValue instanceof Timestamp timestampValue) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateFormat.format(timestampValue);
                result.put(camelCaseColumnName, formattedDateTime);
            } else {
                result.put(camelCaseColumnName, columnValue);
            }
        }
        return result;
    }

    public String convertToCamelCase(String input) {
        String[] parts = input.split("_");
        StringBuilder camelCaseString = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            camelCaseString.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1).toLowerCase());
        }
        return camelCaseString.toString();
    }

    public String convertToSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append("_").append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString().toUpperCase();
    }
}
