package com.github.almostfamiliar.product.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${liquigraph.url}")
  private String jdbcUrl;

  /** Needed for Liquigraph migrations */
  @Bean
  public DataSource dataSource() {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);

    return new HikariDataSource(config);
  }
}
