package com.ffi.backofficehq.controller.master;

import com.ffi.backofficehq.entity.User;
import com.ffi.backofficehq.model.response.DataTableResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public RecipeController(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path = "/api/recipe/dt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "DataTable master recipe", description = "List Master Recipe")
    public ResponseEntity<DataTableResponse> dataTable(User user, @RequestBody Map<String, Object> params) {
        String query = "select distinct rh.recipe_code, rh.recipe_remark, rh.mpcs_group, mh.description, rh.status from m_recipe_header rh join m_mpcs_header mh on mh.mpcs_group = rh.mpcs_group order by rh.status, rh.recipe_code";
        DataTableResponse dtResp = new DataTableResponse().process(query, params, jdbcTemplate);
        return ResponseEntity.ok(dtResp);
    }
}
