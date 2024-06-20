package com.ffi.backofficehq.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MasterDbConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "dataSourceMaster")
    public DataSource dataSourceMaster() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(get("spring.datasource.url"));
        ds.setUsername(get("spring.datasource.username"));
        ds.setPassword(get("spring.datasource.password"));
        ds.setDriverClassName(get("spring.datasource.driverClassName"));
        return ds;
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryMaster() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceMaster());
        em.setPackagesToScan("com.ffi.backofficehq.entity");

        // Specify Hibernate vendor properties
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return em;
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManagerMaster(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Primary
    @Bean(name = "jdbcTemplateMaster")
    public NamedParameterJdbcTemplate masterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSourceMaster());
    }

    public String get(String key) {
        String value = "";
        if (env != null) {
            value = env.getProperty(key);
        }
        return value == null ? "" : value;
    }

    public String get(String key, String defaultValue) {
        String value = "";
        if (env != null) {
            value = env.getProperty(key, defaultValue);
        }
        return value;
    }

}
