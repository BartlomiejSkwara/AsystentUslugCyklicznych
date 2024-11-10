package com.example.uslugicykliczne.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
@EnableScheduling
public class MainConfiguration {

    @Value("${conf.db.schema}")
    private String schemaName;


    @Bean
    public DataSource todosDataSource(DataSourceProperties dataSourceProperties) {
        DataSource ds = dataSourceProperties
                .initializeDataSourceBuilder()
                .build();
        try (Connection connection = ds.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+schemaName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DataSource initialized");
        String url = dataSourceProperties.getUrl().concat("/"+schemaName);
        dataSourceProperties.setUrl(url);

        ds = dataSourceProperties.initializeDataSourceBuilder().build();


        System.out.println("Schema connection created");

        return ds;
    }

}
