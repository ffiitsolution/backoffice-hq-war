package com.ffi.backofficehq.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TransDbConfig {

    @Autowired
    private Environment env;
    
    @Bean(name = "dataSourceTrans")
    public DataSource dataSourceTrans() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(get("spring.datasource-trans.url"));
        ds.setUsername(get("spring.datasource-trans.username"));
        ds.setPassword(get("spring.datasource-trans.password"));
        ds.setDriverClassName(get("spring.datasource.driverClassName"));
        return ds;
    }

    @Bean(name = "jdbcTemplateTrans")
    public NamedParameterJdbcTemplate transJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSourceTrans());
    }
    
    public String get(String key) {
        String value = "";
        if(env != null){
            value = env.getProperty(key);
        }
        return value == null ? "" : value;
    }

    public String get(String key, String defaultValue) {
        String value = "";
        if(env != null){
            value = env.getProperty(key, defaultValue);
        }
        return value;
    }

}
