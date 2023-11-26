package com.virginonline.urlshortener.container;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class AbstractTestContainers {
  public static final DockerImageName CASSANDRA_IMAGE = DockerImageName.parse("cassandra:5.0");
  public static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7.2-rc-alpine");

  @ServiceConnection @Container
  public static final GenericContainer<?> redisContainer =
      new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379);

  @ServiceConnection @Container
  public static final CassandraContainer<?> cassandraContainer =
      new CassandraContainer<>(CASSANDRA_IMAGE).withInitScript("scripts/init-keyspace.cql");

  @Test
  void cassandraConnectionEstablished() {
    assertThat(cassandraContainer.isCreated()).isTrue();
    assertThat(cassandraContainer.isRunning()).isTrue();
  }

  @Test
  void redisConnectionEstablished() {
    assertThat(redisContainer.isCreated()).isTrue();
    assertThat(redisContainer.isRunning()).isTrue();
  }
}
