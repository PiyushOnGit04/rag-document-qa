package com.example.rag_document_qa.config;

import com.pgvector.PGvector;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {

        HikariDataSource hikariDataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        return new DelegatingDataSource(hikariDataSource) {
            @Override
            public Connection getConnection() throws SQLException {
                Connection connection = super.getConnection();
                PGvector.addVectorType(connection);
                return connection;
            }
        };
    }
}