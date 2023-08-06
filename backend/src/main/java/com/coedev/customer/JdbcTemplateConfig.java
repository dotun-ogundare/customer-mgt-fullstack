//package com.coedev.customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//import javax.sql.DataSource;
//
//@Configuration
//public class JdbcTemplateConfig {
//    private final DataSource datasource;
//
//    @Autowired
//    public JdbcTemplateConfig(DataSource datasource) {
//        this.datasource = datasource;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate() {
//        return new JdbcTemplate(datasource);
//    }
//}