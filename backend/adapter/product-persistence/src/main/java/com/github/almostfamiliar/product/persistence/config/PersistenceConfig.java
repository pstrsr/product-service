package com.github.almostfamiliar.product.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import javax.sql.DataSource;
import java.sql.SQLException;

@EnableNeo4jRepositories(basePackages = "com.github.almostfamiliar.product.persistence")
@EntityScan(basePackages = "com.github.almostfamiliar.product.persistence")
@Configuration
public class PersistenceConfig {

  /** Needed for Liquigraph migrations */
  @Bean
  public DataSource dataSource() throws SQLException {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:neo4j:http://localhost:7474");

    return new HikariDataSource(config);
  }
}
