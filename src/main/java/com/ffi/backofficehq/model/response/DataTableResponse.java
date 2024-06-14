package com.ffi.backofficehq.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffi.backofficehq.util.DynamicRowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author USER
 */
public class DataTableResponse {

    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<Map<String, Object>> data;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamicRowMapper dynamicRowMapper = new DynamicRowMapper();

    public DataTableResponse() {
    }

    public DataTableResponse(int draw, int recordsTotal, int recordsFiltered, List<Map<String, Object>> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public DataTableResponse process(String query, Map<String, Object> params, JdbcTemplate jdbcTemplate) {

        List<Map<String, Object>> columns = (List<Map<String, Object>>) params.get("columns");

        if (columns != null) {
            columns.removeIf(column -> {
                Object data = column.get("data");
                return data != null && (data.equals("dtIndex") || data.equals("dtAction"));
            });
        }

        // Parse search
        Map<String, Object> search = (Map<String, Object>) params.get("search");
        List<String> searchValues = new ArrayList<>();
        if (search.containsKey("value")) {
            String searchValue = search.get("value").toString().toLowerCase();
            String wildcardSearchValue = "%" + searchValue + "%";
            for (Map<String, Object> column : columns) {
                searchValues.add(wildcardSearchValue);
            }
        }

        // Build the WHERE clause for search
        StringBuilder whereClause = new StringBuilder();
        if (!searchValues.isEmpty()) {
            whereClause.append("(");
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    whereClause.append(" OR ");
                }
                whereClause.append("LOWER(")
                        .append(dynamicRowMapper.convertToSnakeCase(columns.get(i).get("data").toString()))
                        .append(") LIKE ?");
            }
            whereClause.append(")");
        }

        // Parse order
        List<Map<String, Object>> order = (List<Map<String, Object>>) params.get("order");
        StringBuilder orderByClause = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            int columnIndex = Integer.parseInt(order.get(i).get("column").toString()) - 1; // note: kurangi 1 karena ada kolom numbering
            if(columnIndex == -1){
                columnIndex = 0;
            }
            String columnData = dynamicRowMapper.convertToSnakeCase(columns.get(columnIndex).get("data").toString());
            String dir = order.get(i).get("dir").toString();
            if (i > 0) {
                orderByClause.append(", ");
            }
            orderByClause.append(columnData).append(" ").append(dir);
        }

        // Extract other parameters
        int start = Integer.parseInt(params.get("start").toString());
        int length = Integer.parseInt(params.get("length").toString());

        // Build and execute SQL query for the actual data
        StringBuilder dataQuery = new StringBuilder("SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ");
        dataQuery.append(orderByClause.toString());
        dataQuery.append(") AS rn, t.* FROM (").append(query);

        // Append WHERE clause for search
        if (whereClause.length() > 0) {
            dataQuery.append(" WHERE ").append(whereClause);
        }

        // Add pagination
        dataQuery.append(") t ) WHERE rn > ").append(start).append(" AND rn <= ").append(start + length);

        // Execute the SQL query and retrieve data
        String queryFinal = dataQuery.toString();
        System.out.println("queryFinal: " + queryFinal);
//        System.out.println("searchValues" + Arrays.deepToString(searchValues.toArray()));
        List<Map<String, Object>> result = jdbcTemplate.query(queryFinal, searchValues.toArray(), new DynamicRowMapper());
        System.out.println("result: " + result.size());

        // Calculate total records and filtered records counts
        int totalRecords = getTotalRecords(query, jdbcTemplate);
        int filteredRecords = getFilteredRecords(query, whereClause.toString(), searchValues, jdbcTemplate);

        // Construct the DataTableResult object
        DataTableResponse dataTableResult = new DataTableResponse();
        dataTableResult.setDraw(Integer.parseInt(params.get("draw").toString()));
        dataTableResult.setRecordsTotal(totalRecords);
        dataTableResult.setRecordsFiltered(filteredRecords);
        dataTableResult.setData(result);

        return dataTableResult;
    }

    private int getTotalRecords(String query, JdbcTemplate jdbcTemplate) {
        String totalRecordsQuery = "SELECT COUNT(*) AS totalRecords FROM (" + query + ")";
        return jdbcTemplate.queryForObject(totalRecordsQuery, Integer.class);
    }

    private int getFilteredRecords(String query, String whereClause, List<String> searchValues, JdbcTemplate jdbcTemplate) {
        if (whereClause.isEmpty()) {
            return getTotalRecords(query, jdbcTemplate);
        } else {
            String filteredRecordsQuery = "SELECT COUNT(*) AS filteredRecords FROM (" + query + ") WHERE " + whereClause;
            return jdbcTemplate.queryForObject(filteredRecordsQuery, Integer.class, searchValues.toArray());
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("draw", draw);
        resultMap.put("recordsTotal", recordsTotal);
        resultMap.put("recordsFiltered", recordsFiltered);
        resultMap.put("data", data);
        return resultMap;
    }

    // Getters and Setters
    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "DataTableResult{"
                + "draw=" + draw
                + ", recordsTotal=" + recordsTotal
                + ", recordsFiltered=" + recordsFiltered
                + ", data=" + data
                + '}';
    }
}
