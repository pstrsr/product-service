package com.github.almostfamiliar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.Neo4jContainer;

@Slf4j
class TestContainerInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {
  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    final Neo4jContainer<?> neo4jContainer =
        new Neo4jContainer<>("neo4j:4.2.4").withoutAuthentication();
    neo4jContainer.start();
    configurableApplicationContext.addApplicationListener(
        (ApplicationListener<ContextClosedEvent>) event -> neo4jContainer.stop());
    TestPropertyValues.of(
            "liquigraph.url=" + "jdbc:neo4j:" + neo4jContainer.getHttpUrl(),
            "spring.neo4j.uri=" + neo4jContainer.getBoltUrl(),
            "spring.neo4j.authentication.username=neo4j",
            "spring.neo4j.authentication.password=" + neo4jContainer.getAdminPassword())
        .applyTo(configurableApplicationContext.getEnvironment());
  }
}
