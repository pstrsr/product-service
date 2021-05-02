package com.github.almostfamiliar.product.persistence;

import com.github.almostfamiliar.product.persistence.mapper.CategoryPersistenceMapperImpl;
import com.github.almostfamiliar.product.persistence.mapper.ProductPersistenceMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories(basePackages = "com.github.almostfamiliar.product.persistence")
@EntityScan(basePackages = "com.github.almostfamiliar.product.persistence")
@Slf4j
@SpringBootApplication
@Import({
  CategoryPersistenceMapperImpl.class,
  ProductPersistenceMapperImpl.class,
  CategoryDao.class,
  ProductDao.class
})
public class TestConfig {}
