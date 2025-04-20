package com.sameer.ChatApp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource mainDataSource(){
        DataSource dataSource = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/chatapp")
                .username("root")
                .password("password")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        System.out.println("Main DataSource initialized with URL: jdbc:mysql://localhost:3306/chatapp");
        return dataSource;
    }

//    @Bean
//    @Qualifier("initDataSource")
//    @ConfigurationProperties(prefix = "spring.init.datasource")
//    public DataSource initDataSource() {
//        DataSource dataSource = DataSourceBuilder.create()
//                .url("jdbc:mysql://localhost:3306?createDatabaseIfNotExist=true")
//                .username("root")
//                .password("password")
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .build();
//        System.out.println("Init DataSource initialized with URL: jdbc:mysql://localhost:3306?createDatabaseIfNotExist=true");
//        return dataSource;
//    }
}